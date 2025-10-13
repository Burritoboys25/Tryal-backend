package com.backend.tryal.booking.dto;

import lombok.Data;

@Data
public class BookingPatchDTO {
  private String stripeTransferId;
  private String bookingStatus;
  private Integer party;
}
