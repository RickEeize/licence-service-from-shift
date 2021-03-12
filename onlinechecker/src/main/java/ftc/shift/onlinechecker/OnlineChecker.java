package ftc.shift.onlinechecker;

import ftc.shift.onlinechecker.exception.LicenceException;
import lombok.NonNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OnlineChecker {
    public static boolean isLicenceAvailable(@NonNull String url,
                                             @NonNull String licence,
                                             @NonNull String productType,
                                             @NonNull String productVersion)
            throws IOException, InterruptedException, LicenceException {


        HttpClient httpClient = HttpClient.newHttpClient();

        var uri = String.format(url + "/licences/check?productType=%s&productVersion=%s", productType, productVersion);

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(uri))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(licence))
                .build();

        var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new LicenceException(response.body());
        }
    }
}
