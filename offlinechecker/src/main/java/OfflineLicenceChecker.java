package offline.check.licence.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;

public class OfflineLicenceChecker {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private static boolean checkMarks(String key){
        for (int i = 1; i <= key.length() / 6; i++)
            if(key.charAt(i * 6 - 1) != (char) (key.charAt((i - 1) * 6) + 1))
                return false;
        return true;
    }

    private static String removeMarksFromKey(String key){
        StringBuilder builder = new StringBuilder(key);
        for (int i = 1; i <= key.length() / 6; i++)
            builder.delete(i * 6 - i, i * 6 - i + 1);

        return builder.toString();
    }

    private static OfflineLicence getLicenseFromString(String licenseString) throws LicenceMarkersException {
        if(!checkMarks(licenseString))
            throw new LicenceMarkersException("Licence have incorrect markers!");

        var encodedLicense = new String(Base64.getDecoder().decode(removeMarksFromKey(licenseString).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return gson.fromJson(encodedLicense, OfflineLicence.class);
    }


    @Getter
    //@AllArgsConstructor
    public static class OfflineLicence {
        @NonNull
        @Expose
        private final Date createDate;

        @NonNull
        @Expose
        private final Date endDate;

        @NonNull
        @Expose
        private final String type;

        public OfflineLicence(Date createDate, Date endDate, String type){
            this.createDate = createDate;
            this.endDate = endDate;
            this.type = type;
        }
    }

    public static class LicenceMarkersException extends Exception {
        public LicenceMarkersException(String message, Throwable cause) {
            super(message, cause);
        }

        public LicenceMarkersException(String message) {
            super(message);
        }

        public LicenceMarkersException(Throwable cause) {
            super(cause);
        }
    }
}
