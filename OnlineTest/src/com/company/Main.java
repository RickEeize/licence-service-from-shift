package com.company;

import ftc.shift.onlinechecker.OnlineChecker;

public class Main {

    public static void main(String[] args) {

        var url = "http://localhost:8081";
        var licence = "eyJpZCI6IjcxMjFiNmRlLWViZDEtNGE0Ni04OWFjLTRjZmVkNmI0NDU0ZSIsImxpY2VuY2VLZXkiOiJDSVBVbXhGWE5xeWJsd1NNRDA1NXRVUTBXQmhaNjc4UURDaUUrNVIvbGE2VFNldE5mNG5TR0NPMmFaYTlxa252SmZuZjVOQkdHUEtoeCttNGR4UUlveS8wMHNmNU93WEVJZXBRRlU2cnNHZUFla3QyTHdMa2FjOEVBMTRvR0lZOU9yVU0xa0FyUStMRXJkRWpmN2Y1cndqend5U05kb0VTcDBSZ29tcXp4QkY2eXBDMG9salZVUXFJcCtLTUdHWGhWcjh2dW5mL0hid0Z4enV6dllYT0NmMldQdnhjb3FIZzhKbGZzN0ExSFhKWEo4L2t0UHdrNE11WUdRSlR6SndDbHdVYzJmbzN6S1E4U3JGY3cxVUFhS0xiTlZrcU8zY3ZET2gwV2JZajYxM05YN0JXVG03R29QdEdTWEh1YkUxa00xVVJNOU1tRTYxQkY5bXBHT3kra1FcdTAwM2RcdTAwM2QiLCJjcmVhdGVEYXRlIjoi0LzQsNGALiAxMiwgMjAyMSIsImVuZERhdGUiOiLQuNGO0L0uIDIwLCAyMDIxIiwidHlwZSI6InQxIiwibnVtYmVyT2ZMaWNlbmNlcyI6MSwicHJvZHVjdFR5cGUiOiJJZGVhIiwicHJvZHVjdFZlcnNpb24iOiIxLjAifQ==";
        var productType = "Idea";
        var productVersion = "1.0";

        boolean res = false;
        try {
            res = OnlineChecker.isLicenceAvailable(url,licence,productType,productVersion);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(res);
    }
}
