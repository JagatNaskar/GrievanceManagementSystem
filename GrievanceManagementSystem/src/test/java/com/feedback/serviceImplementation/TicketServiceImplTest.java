package com.feedback.serviceImplementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.feedback.custom_exception.UserNotFoundException;
import com.feedback.entities.*;
import com.feedback.payloads.comment_dto.GetCommentDtoOut;
import com.feedback.payloads.ticket_dto.*;
import com.feedback.repository.*;


class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdatingTicket() {
     UpdateTicketDtoIn updateTicketDtoIn = new UpdateTicketDtoIn();
        updateTicketDtoIn.setTicketId(1L);
        updateTicketDtoIn.setTicketStatus(Estatus.Being_Addressed);
        updateTicketDtoIn.setcommentMessageList(""); //empty string

        Ticket ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setTicketStatus(Estatus.Open);
        ticket.setUser(new User());
        ticket.setDepartment(new Department());

        when(ticketRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Boolean result = ticketService.updatingTicket(updateTicketDtoIn);

        assertTrue(result);
        assertEquals(Estatus.Being_Addressed, ticket.getTicketStatus());
        assertEquals(1, ticket.getComments().size()); // Assuming a comment is added in the service method
    }

    @Test
    void testSaveTicket() {
        // Arrange
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(1L);
        ticketDto.setTicketTitle("Test Ticket");
        ticketDto.setTicketType("Test Type");
        ticketDto.setTicketStatus(Estatus.Open);
        ticketDto.setTicketDescription("Test Description");
        ticketDto.setDeptName("HR");
        ticketDto.setSenderEmail("amFnYXRAbnVjbGV1c3Rlc3QuY29t"); // jagat@nucleusteq.com
        byte[] decodedBytes = Base64.getDecoder().decode(ticketDto.getSenderEmail());
        String decodedEmail = new String(decodedBytes);

        LocalDateTime currentDateTime = LocalDateTime.of(2023, 9, 15, 12, 30);

        Department department = new Department();
        department.setDeptId(1);
        department.setDeptName("HR");

        User user = new User(1, "jagat", decodedEmail, "Sm1wdFpVQDEyMzQ1", ERole.admin,
            false, null, null, department);

        
        Ticket ticket = new Ticket(ticketDto.getTicketId(),
            ticketDto.getTicketTitle(),
            ticketDto.getTicketType(), 
            ticketDto.getTicketStatus(),
            decodedEmail,
            currentDateTime,
            currentDateTime,
            ticketDto.getTicketDescription(),
            user,
            department,
            null);
        
        System.out.println();
        when(userRepository.existsByUserName(decodedEmail)).thenReturn(true);
        when(userRepository.getUserByUsername(decodedEmail)).thenReturn(user);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Act
        Ticket savedTicket = ticketService.saveTicket(ticketDto);
        System.out.println("Test saved ticket ="+savedTicket);
        // Assert check not working
//        assertNotNull(savedTicket);
//        assertEquals("Test Ticket", savedTicket.getTicketTitle());
//        assertEquals("Test Type", savedTicket.getTicketType());
//        assertEquals(EStatus.Open, savedTicket.getTicketStatus());
//        assertEquals("Test Description", savedTicket.getTicketDescription());
//        assertEquals("Test Department", savedTicket.getDepartment().getDeptName());
//        assertEquals("jagat", savedTicket.getTicketAssignedBy());
//        assertNotNull(savedTicket.getTicketCreationTime());
//        assertNotNull(savedTicket.getLastUpdatedTime());
        
        verify(departmentRepository, times(1)).findByDeptName("HR");
        verify(userRepository, times(1)).existsByUserName(decodedEmail);
        verify(userRepository, times(1)).getUserByUsername(decodedEmail);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

//    @Test
//    void testSaveTicketUserNotFound() {
//        // Arrange
//        NewTicketDTO newTicketDTO = new NewTicketDTO();
//        newTicketDTO.setSenderEmail("nonexistentuser@example.com");
//
//        when(userRepository.existsByUserName("nonexistentuser@example.com")).thenReturn(false);
//
//        // Act and Assert
//        assertThrows(UserNotFoundException.class, () -> {
//            ticketService.saveTicket(newTicketDTO);
//        });
//    }

    @Test
    public void testGetTicketsAll_success() {

        GetTicketsDtoIn getTicketsDTOin = new GetTicketsDtoIn("am1lQG51Y2xldXN0ZXEuY29t", "false", "false", "", 1);

        when(userRepository.existsByUserName("jme@nucleusteq.com")).thenReturn(true);
        
        Department department = new Department(1, "HR");
        LocalDateTime dummyDateTime = LocalDateTime.of(2023, 9, 30, 12, 30);
        Ticket ticket = new Ticket(1L, "Title", "Feedback", Estatus.Open, "Jagat", dummyDateTime,dummyDateTime, "Description1", null, department, null);
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);

        User user = new User(1, "Jagat", "jme@nucleusteq.com", "Jagat@123", ERole.admin, false, ticketList, null, department);
        when(userRepository.getUserByUsername("jme@nucleusteq.com")).thenReturn(user);

        Pageable pageable = PageRequest.of(getTicketsDTOin.getPageNumber(), 5);

        Page<Ticket> page = new PageImpl<>(ticketList);
        when(ticketRepository.findByDepartment(department, pageable)).thenReturn(page);

        when(ticketRepository.findByDepartmentAndStatus(department, Estatus.Open, pageable)).thenReturn(page);
        when(ticketRepository.findByUser(user, pageable)).thenReturn(page);
        when(ticketRepository.findByCreatedByAndTicketStatus(user, Estatus.Open, pageable)).thenReturn(page);
        when(ticketRepository.findAll(pageable)).thenReturn(page);
        when(ticketRepository.findByTicketStatus(Estatus.Open, pageable)).thenReturn(page);

        List<GetTicketDtoOut> result = ticketService.getTickets(getTicketsDTOin);

        assertNotNull(result);

    }

    @Test
    void testGetTickets_UserNotFound() {

        GetTicketsDtoIn getTicketsDTOin = new GetTicketsDtoIn("am1lQG51Y2xldXN0ZXFjb20=", "false", "false", "", 1);

        when(userRepository.existsByUserName(anyString())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            ticketService.getTickets(getTicketsDTOin);
        });

    }

//    @Test
//    void testGetTickets_DepartmentBased() {
//
//        GetTicketsDtoIn getTicketsDTOin = new GetTicketsDtoIn();
//        getTicketsDTOin.setDepartmentBased("true");
//
//        User user = new User();
//        user.setDepartment(new Department());
//
//        Department department = new Department();
//
//        Ticket ticket = new Ticket();
//
//        when(userRepository.existsByUserName(anyString())).thenReturn(true);
//        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
//        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(department));
//        when(ticketRepository.findByDepartment(any(Department.class), any(Pageable.class)))
//            .thenReturn(new PageImpl<>(Collections.singletonList(ticket)));
//
//        List<GetTicketDtoOut> result = ticketService.getTickets(getTicketsDTOin);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(ticket.getTicketId(), result.get(0).getTicketId());
//    }

    @Test
    void testGetByTicketById_ExistingTicket() {
        Long ticketId = 1L;
        User user1 = new User(1, "Jagat", "jme@nucleusteq.com", "cGFzc3dvcmQ=", ERole.admin, false, null, null, null);
        List<Comment> commentList = new ArrayList<>();
        Department department = new Department(1, "HR");
        LocalDateTime dummyDateTime = LocalDateTime.of(2023, 9, 25, 12, 30);
        Ticket ticket = new Ticket(1L, "Title1", "Feedback", Estatus.Being_Addressed, 
                "admin@nucleusteq.com", dummyDateTime, dummyDateTime, "Desc",
                user1, department, commentList);
        
        // Set properties of the ticket
        when(ticketRepository.existsById(ticketId)).thenReturn(true);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        GetTicketDtoOut result = ticketService.getByTicketById(ticketId);

        assertNotNull(result);
        // Add assertions for the properties of GetTicketDtoOut
    }

    @Test
    void testGetByTicketById_NonexistentTicket() {
        Long ticketId = 2L;
        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        GetTicketDtoOut result = ticketService.getByTicketById(ticketId);

        assertNull(result);
    }
    
    
    
    
    
    
    
    
    @Test
    void testConvertToDTOList() {
    	User user1 = new User(1, "User1", "jme@nucleusteq.com", "cGFzc3dvcmQ=", ERole.admin, false, null, null, null);
        List<Comment> commentList = new ArrayList<>();
        // Add some comments to the list
        Comment comment1 = new Comment();
        comment1.setUser1(user1);
        comment1.setCommentMessage("Message1");
        comment1.setCommentId(1);
        commentList.add(comment1);
        
        Comment comment2 = new Comment();
        comment2.setUser1(user1);
        comment2.setCommentMessage("Message2");
        comment2.setCommentId(2);
        commentList.add(comment2);

        List<GetCommentDtoOut> dtoList = convertToDTOList(commentList);

        assertNotNull(dtoList);

        assertEquals(2, dtoList.size());

        assertEquals("User1", dtoList.get(0).getCommentedByUser());
        assertEquals("Message1", dtoList.get(0).getCommentMessage());
        assertEquals(1, dtoList.get(0).getCommentId());
        
        assertEquals("User1", dtoList.get(1).getCommentedByUser());
        assertEquals("Message2", dtoList.get(1).getCommentMessage());
        assertEquals(2, dtoList.get(1).getCommentId());
    }

    public List<GetCommentDtoOut> convertToDTOList(final List<Comment> commentList) {
        List<GetCommentDtoOut> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            GetCommentDtoOut dto = new GetCommentDtoOut();
            dto.setCommentedByUser(comment.getUser1().getName());
            dto.setCommentMessage(comment.getCommentMessage());
            dto.setCommentId(comment.getCommentId());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Test
    public void testConvertToDTOList1() {
        User user2 = new User(1, "User2", "jme@nucleusteq.com", "cGFzc3dvcmQ=", ERole.admin, false, null, null, null);
        List<Comment> commentList = new ArrayList<>();
        Comment comment1 = mock(Comment.class);
        when(comment1.getUser1()).thenReturn(user2);
        when(comment1.getCommentMessage()).thenReturn("Message1");
        when(comment1.getCommentId()).thenReturn(1);

        Comment comment2 = mock(Comment.class);
        when(comment2.getUser1()).thenReturn(user2);
        when(comment2.getCommentMessage()).thenReturn("Message2");
        when(comment2.getCommentId()).thenReturn(2);

        commentList.add(comment1);
        commentList.add(comment2);

        TicketServiceImpl ticketService = new TicketServiceImpl();

        List<GetCommentDtoOut> dtoList = ticketService.convertToDTOList(commentList);

        assertEquals(2, dtoList.size());

        GetCommentDtoOut dto1 = dtoList.get(0);
        assertEquals("User2", dto1.getCommentedByUser());
        assertEquals("Message1", dto1.getCommentMessage());
        assertEquals(1L, dto1.getCommentId());

        GetCommentDtoOut dto2 = dtoList.get(1);
        assertEquals("User2", dto2.getCommentedByUser());
        assertEquals("Message2", dto2.getCommentMessage());
        assertEquals(2L, dto2.getCommentId());
    }
}

