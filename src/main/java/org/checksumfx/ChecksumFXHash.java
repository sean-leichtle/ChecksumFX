/*
 * ChecksumFX
 *
 * Copyright 2024 Sean Leichtle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.checksumfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumFXHash {

    private File file;
    private String algorithm;

    ChecksumFXHash(File file, String algorithm) {
        this.file = file;
        this.algorithm = algorithm;
    }

    private byte[] calculateHashValue() {
        MessageDigest messageDigest;
        try (FileInputStream fileInputStream = new FileInputStream(this.file.getAbsolutePath())) {
            messageDigest = MessageDigest.getInstance(this.algorithm);
            byte[] buffer = new byte[1024];
            int read;
            do {
                read = fileInputStream.read(buffer);
                if (read > 0) {
                    messageDigest.update(buffer, 0, read);
                }
            } while (read != -1);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest();
    }

    private String hashToString(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < arr.length; i++) {
            sb.append(Integer.toString((arr[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    String getHashValue() {
        return hashToString(calculateHashValue());
    }
}
