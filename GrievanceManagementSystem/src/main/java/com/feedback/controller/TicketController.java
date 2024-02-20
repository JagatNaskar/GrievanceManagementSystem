package com.feedback.controller;

import com.feedback.payloads.ticket_dto.GetTicketsDtoIn;
import com.feedback.payloads.ticket_dto.TicketDto;
import com.feedback.payloads.ticket_dto.UpdateTicketDtoIn;
import com.feedback.payloads.ticket_dto.GetTicketDtoOut;
import com.feedback.service.TicketService;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ticket Controller class.
 *
 * @author jagat.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/tickets")
public class TicketController {
  /**
   * ticket service object initialization.
   */
  @Autowired
  private TicketService ticketService;

  /**
   * adding tickets to databases.
   *
   * @param ticket
   *
   * @return saved ticket string.
   */
  @PostMapping("/addTicket")
  public ResponseEntity<?> addTickets(@RequestBody final TicketDto ticket) {
    ticketService.saveTicket(ticket);
    return ResponseEntity.status(HttpStatus.OK).body("Ticket Added");
  }

  /**
   * getting tickets based on certain condition.
   *
   * @param getTicketsDTOin
   *
   * @return list of ticket.
   */
  @PostMapping("/getAllTicket")
  public ResponseEntity<?> getTickets(
      @Valid @RequestBody final GetTicketsDtoIn getTicketsDTOin
  ) {
    List<GetTicketDtoOut> allTicketList = ticketService
        .getTickets(getTicketsDTOin);
    return ResponseEntity.status(HttpStatus.OK).body(allTicketList);
  }

  /**
   * updating status and comment on the ticket.
   *
   * @param updateTicketDtoIn
   *
   * @return updateTicket.
   */
  @PostMapping("/updateTicket")
  public ResponseEntity<?> updateTicket(
      @Valid @RequestBody final UpdateTicketDtoIn updateTicketDtoIn
  ) {
    ticketService.updatingTicket(updateTicketDtoIn);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body("Ticket Updated.");
    }

  /**
   * set it to work in the test cases.
   *
   * @param ticketServicee
   *
   */
  public void setTicketService(final TicketService ticketServicee) {
    this.ticketService = ticketServicee;
  }

  /**
   * Get ticket by its id.
   *
   * @param ticketId
   *
   * @return string.
   */
  @GetMapping("/getIcketById/{ticketId}")
  public ResponseEntity<?> getTicketById(
      @PathVariable("ticketId") final Long ticketId) {
    GetTicketDtoOut ticketDTOout = ticketService.getByTicketById(ticketId);
      return ResponseEntity.status(HttpStatus.OK).body(ticketDTOout);
  }
}
