package com.example.trelloapi.service;

import com.example.trelloapi.model.Board;
import com.example.trelloapi.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBoardSuccess() {
        Board board = new Board();
        board.setName("Test Board");
        board.setDescription("Description");

        when(boardRepository.save(any(Board.class))).thenReturn(board);

        Board savedBoard = boardService.save(board);

        assertNotNull(savedBoard);
        assertEquals("Test Board", savedBoard.getName());
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void testSaveBoardNameTooShort() {
        Board board = new Board();
        board.setName("ab");
        board.setDescription("Description");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardService.save(board);
        });

        assertEquals("Nome do Board deve conter no mínimo 3 caracteres.", exception.getMessage());
        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    void testUpdateBoardSuccess() {
        Board existingBoard = new Board();
        existingBoard.setId(1L);
        existingBoard.setName("Old Name");
        existingBoard.setDescription("Old Description");

        Board updatedDetails = new Board();
        updatedDetails.setName("New Name");
        updatedDetails.setDescription("New Description");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(existingBoard));
        when(boardRepository.save(any(Board.class))).thenReturn(updatedDetails);

        Board result = boardService.update(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        verify(boardRepository, times(1)).findById(1L);
        verify(boardRepository, times(1)).save(existingBoard);
    }

    @Test
    void testUpdateBoardNotFound() {
        Board updatedDetails = new Board();
        updatedDetails.setName("New Name");
        updatedDetails.setDescription("New Description");

        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardService.update(1L, updatedDetails);
        });

        assertEquals("Board não encontrado com ID: 1", exception.getMessage());
        verify(boardRepository, times(1)).findById(1L);
        verify(boardRepository, never()).save(any(Board.class));
    }
}
