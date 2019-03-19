package io._57blocks.email.params;

import lombok.Data;

@Data
public class Attachment {
  private String filename;
  private byte[] attachmentBytes;
  private String contentType;
}
