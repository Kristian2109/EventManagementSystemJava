package com.projects.eventsbook.service;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;

public interface QrCodeGenerator {
    String generateQrCode(String data) throws WriterException, IOException;
}
