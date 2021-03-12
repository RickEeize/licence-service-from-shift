package ftc.shift.sample.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.LicenceDecodeException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.UUID;

public class LicenceUtil {

    private static final String KEY_ALGORITHM = "RSA";

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private static KeyPairGenerator keyPairGenerator;
    private static Cipher cipher;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    private static String addMarksToKey(String key) {
        StringBuilder builder = new StringBuilder(key);

        for (int i = 1; i <= key.length() / 5; i++)
            builder.insert(i * 5 + (i - 1), (char) (key.charAt((i - 1) * 5) + 1));

        return builder.toString();
    }

    private static String removeMarksFromKey(String key){
        StringBuilder builder = new StringBuilder(key);
        for (int i = 1; i <= key.length() / 6; i++)
            builder.delete(i * 6 - i, i * 6 - i + 1);

        return builder.toString();
    }

    private static boolean checkMarks(String key){
        for (int i = 1; i <= key.length() / 6; i++)
            if(key.charAt(i * 6 - 1) != (char) (key.charAt((i - 1) * 6) + 1))
                return false;
        return true;
    }

    private static String encryptPublicKey(PublicKey publicKey) throws LicenceGeneratorException {
        var data = publicKey.getEncoded();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            /*
             * Весь ключ не влезет по длине поэтому шифруем пополам
             */
            cipher.update(data, 0, data.length / 2);
            var licenseKey1 = cipher.doFinal();

            cipher.update(data, data.length / 2, data.length / 2);
            var licenseKey2 = cipher.doFinal();

            var licenseKey = ArrayUtils.addAll(licenseKey1, licenseKey2);

            return Base64.getEncoder().encodeToString(licenseKey);

        } catch (Exception e) {
            throw new LicenceGeneratorException("Public key generation err", e);
        }
    }

    private static PublicKey decryptPublicKey(String encodedKey, PrivateKey privateKey) throws LicenceDecodeException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            var licenseKey = Base64.getDecoder().decode(encodedKey);

            cipher.update(licenseKey, 0, licenseKey.length / 2);
            // Получаем public_key
            byte[] decoded1 = cipher.doFinal();

            cipher.update(licenseKey, licenseKey.length / 2, licenseKey.length / 2);
            byte[] decoded2 = cipher.doFinal();

            var decoded = ArrayUtils.addAll(decoded1, decoded2);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePublic(spec);

        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            throw new LicenceDecodeException("Public key decoding err", e);
        }

    }

    private static PrivateKey decodePrivateKetFromString(String encodedKey) throws LicenceDecodeException {
        try {
            var decoded = Base64.getDecoder().decode(encodedKey);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(spec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new LicenceDecodeException(e);
        }

    }

    public static String generateLicenseString(Licence license) {
        return addMarksToKey(Base64.getEncoder().encodeToString(gson.toJson(license).getBytes(StandardCharsets.UTF_8)));
    }

    //Подразумевается что пользователь утилиты должет логировать экспешены
    public static Licence generateLicence(Long userId, Long durationInDay) throws LicenceGeneratorException {

        keyPairGenerator.initialize(1024, new SecureRandom());

        var keyPair = keyPairGenerator.generateKeyPair();

        // В license_key будем хранить public_key и проверять его через шифровку-дешифровку
        var publicKey = keyPair.getPublic();
        var privateKey = keyPair.getPrivate();


        var licenseID = UUID.randomUUID();//Вот это пихаем в бд как праймари кей.

        var licenseKey = encryptPublicKey(publicKey);

        return new Licence(licenseID,
            Base64.getEncoder().encodeToString(privateKey.getEncoded()),
            licenseKey,
            Date.valueOf(LocalDate.now()),
            Date.valueOf(LocalDate.now().plusDays(durationInDay)),
            userId,
            "default",
            null);
    }


    // Этот метод достает из строки лицензии экземпляр лицензии
    //Подразумевается что пользователь утилиты должет логировать экспешены

    public static PublicLicence getLicenseFromString(String licenseString) throws LicenceDecodeException {
        try {
            var encodedLicense = new String(Base64.getDecoder().decode(removeMarksFromKey(licenseString).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            return gson.fromJson(encodedLicense, PublicLicence.class);
        } catch (JsonSyntaxException e) {
            throw new LicenceDecodeException("Wrong licence string", e);
        }

    }

    //Подразумевается что пользователь утилиты должет логировать экспешены
    public static boolean isLicenceCorrect(PublicLicence publicLicense, String privateKeyString) throws LicenceDecodeException {

        try {
            var privateKey = LicenceUtil.decodePrivateKetFromString(privateKeyString);

            var publicKey = LicenceUtil.decryptPublicKey(publicLicense.getLicenceKey(), privateKey);

            // Шифруем-дешифруем
            Cipher decode = Cipher.getInstance(KEY_ALGORITHM);
            Cipher encode = Cipher.getInstance(KEY_ALGORITHM);


            decode.init(Cipher.DECRYPT_MODE, privateKey);
            encode.init(Cipher.ENCRYPT_MODE, publicKey);


            int length = 30;
            String randomTestString = RandomStringUtils.random(length, true, true);

            encode.update(randomTestString.getBytes(StandardCharsets.UTF_8));
            decode.update(encode.doFinal());

            return randomTestString.equals(new String(decode.doFinal(), StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException |
            InvalidKeyException |
            NoSuchPaddingException |
            BadPaddingException |
            IllegalBlockSizeException e) {
            throw new LicenceDecodeException(e);
        }
    }

    public static PublicLicence getPublicLicence(Licence licence) {
        return new PublicLicence(licence.getId(),
            licence.getLicenceKey(),
            licence.getCreateDate(),
            licence.getEndDate(),
            licence.getType(),
            licence.getNumberOfLicences());
    }

    @Getter
    @AllArgsConstructor
    public static class PublicLicence {
        @NonNull
        @Expose
        private final UUID id;

        @NonNull
        @Expose
        private final String licenceKey;

        @NonNull
        @Expose
        private final Date createDate;

        @NonNull
        @Expose
        private final Date endDate;

        @NonNull
        @Expose
        private final String type;

        @Expose
        private final Integer numberOfLicences;
    }
}
