package io._57blocks.email;

import lombok.Data;

@Data
public class Attachment {
  private String filename;
  private byte[] attachmentBytes;
  private String contentType;
}
