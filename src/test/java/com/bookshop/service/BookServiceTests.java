package com.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookshop.dto.book.request.CreateBookRequestDto;
import com.bookshop.dto.book.response.BookDto;
import com.bookshop.dto.book.response.BookDtoWithoutCategoryIds;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.model.Book;
import com.bookshop.model.Category;
import com.bookshop.repository.book.BookRepository;
import com.bookshop.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    private static final String CANT_FIND_BOOK_MESSAGE = "Can't find book by id: ";
    private static final Long ID_ONE = 1L;
    private static final Long ID_TWO = 2L;
    private static final Long ID_THREE = 3L;
    private static final Long ID_FOUR = 4L;
    private static final Long ID_FIVE = 5L;
    private static final Long INCORRECT_ID = 100L;
    private static final int ONCE = 1;
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);
    private static final Set<Category> FIRST_CATEGORY_SET =
            new HashSet<>(Set.of(new Category(ID_ONE)));
    private static final Set<Category> SECOND_CATEGORY_SET =
            new HashSet<>(Set.of(new Category(ID_TWO)));

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book by correct data")
    public void save_AllCorrectData_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no description")
    public void save_WithoutDescription_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setDescription(null);
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no cover image")
    public void save_WithoutCoverImage_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setCoverImage(null);
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with empty categories id")
    public void save_WithEmptyCategoriesId_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setCategoryIds(new HashSet<>());
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no description and cover image")
    public void save_WithoutDescriptionAndCoverImage_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setDescription(null);
        requestDto.setCoverImage(null);
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no description and cover image, empty categories id")
    public void save_WithoutDescriptionAndCoverImageAndEmptyCategories_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setDescription(null);
        requestDto.setCoverImage(null);
        requestDto.setCategoryIds(new HashSet<>());
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no description and empty categories id")
    public void save_WithoutDescriptionAndEmptyCategoriesId_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setDescription(null);
        requestDto.setCategoryIds(new HashSet<>());
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Save book with no cover image and empty categories id")
    public void save_WithoutCoverImageAndEmptyCategoriesId_CorrectDtoReturned() {
        CreateBookRequestDto requestDto = createValidCreateBookRequestDto();
        requestDto.setCoverImage(null);
        requestDto.setCategoryIds(new HashSet<>());
        Book expectedBookWithoutId = getBookFromCreateBookRequestDto(requestDto);
        Book expectedBookWithId = getBookFromCreateBookRequestDto(requestDto);
        expectedBookWithId.setId(ID_ONE);
        BookDto expected = getBookDtoFromBook(expectedBookWithId);

        when(bookMapper.toEntity(requestDto)).thenReturn(expectedBookWithoutId);
        when(bookRepository.save(expectedBookWithoutId)).thenReturn(expectedBookWithId);
        when(bookMapper.toDto(expectedBookWithId)).thenReturn(expected);
        
        BookDto actual = bookService.save(requestDto);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).save(expectedBookWithoutId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toEntity(requestDto);
        verify(bookMapper, times(ONCE)).toDto(expectedBookWithId);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Get correct book dto with correct id")
    public void getById_WithValidId_CorrectDtoReturned() {
        Long bookId = ID_ONE;
        Book book = createValidBook(bookId);
        BookDto expected = getBookDtoFromBook(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        
        BookDto actual = bookService.getById(bookId);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toDto(book);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to get correct book dto with incorrect id")
    public void getById_WithInValidId_ExceptionThrown() {
        String expected = CANT_FIND_BOOK_MESSAGE + INCORRECT_ID;

        when(bookRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(INCORRECT_ID));

        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findById(INCORRECT_ID);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Update book with correct id")
    public void update_WithValidIdAndValidData_CorrectDtoReturned() {
        CreateBookRequestDto request = createValidCreateBookRequestDto();
        Long bookId = ID_ONE;
        Book book = createValidBook(bookId);
        Book updatedBookWithoutCategories =
                updateBookByCreateBookRequestDtoWithoutCategories(book, request);
        Book updatedBook = updateBookByCreateBookRequestDtoWithoutCategories(book, request);
        updatedBook.setCategories(request.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
        BookDto expected = getBookDtoFromBook(updatedBook);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doAnswer(invocationOnMock -> {
            updatedBookWithoutCategories.setCategories(request.getCategoryIds().stream()
                    .map(Category::new)
                    .collect(Collectors.toSet()));
            return null;
        }).when(bookMapper).setCategories(updatedBookWithoutCategories, request);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);
        
        BookDto actual = bookService.update(bookId, request);
        
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findById(bookId);
        verify(bookRepository, times(ONCE)).save(updatedBook);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).setCategories(updatedBookWithoutCategories, request);
        verify(bookMapper, times(ONCE)).toDto(updatedBook);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to update book with incorrect id")
    public void update_WithInValidIdAndValidData_ExceptionThrown() {
        CreateBookRequestDto request = createValidCreateBookRequestDto();
        String expected = CANT_FIND_BOOK_MESSAGE + INCORRECT_ID;

        when(bookRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(INCORRECT_ID, request));

        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findById(INCORRECT_ID);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Find list of 0 books")
    public void findAll_NoBooksCorrectPageable_ReturnsEmptyList() {
        List<BookDto> expected = new ArrayList<>();
        when(bookRepository.findAll(STANDART_PAGEABLE)).thenReturn(Page.empty());
        
        List<BookDto> actual = bookService.findAll(STANDART_PAGEABLE);

        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAll(STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Find list of 1 book")
    public void findAll_OneBookCorrectPageable_ReturnsCorrectList() {
        Book book = createValidBook(ID_ONE);
        List<Book> books = List.of(book);
        Page<Book> page = new PageImpl<>(books, STANDART_PAGEABLE, books.size());
        BookDto bookDto = getBookDtoFromBook(book);
        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto);

        when(bookRepository.findAll(STANDART_PAGEABLE)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        
        List<BookDto> actual = bookService.findAll(STANDART_PAGEABLE);

        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAll(STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toDto(book);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Delete book with correct id")
    public void deleteById_CorrectId_Success() {
        Long bookId = ID_ONE;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);
        
        bookService.deleteById(bookId);
        
        verify(bookRepository, times(ONCE)).existsById(bookId);
        verify(bookRepository, times(ONCE)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to delete book with correct id")
    public void deleteById_InCorrectId_ThrowsException() {
        String excepted = CANT_FIND_BOOK_MESSAGE + INCORRECT_ID;
        when(bookRepository.existsById(INCORRECT_ID)).thenReturn(false);
        
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(INCORRECT_ID));

        String actual = exception.getMessage();
        assertEquals(excepted, actual);
        verify(bookRepository, times(ONCE)).existsById(INCORRECT_ID);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Find one book by category with correct category in db")
    public void getBooksByCategoryId_OneBookWithRightCategory_ReturnsCorrectList() {
        Book book = createValidBook(ID_ONE);
        BookDtoWithoutCategoryIds bookDto = getDtoWithoutCategoryIds(book);
        Long categoryId = ID_TWO;
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);

        when(bookRepository.findAllByCategoryId(categoryId, STANDART_PAGEABLE))
                .thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);
        
        List<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(
                categoryId, STANDART_PAGEABLE);

        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAllByCategoryId(categoryId, STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(ONCE)).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Find 5 books by category with correct category only in db")
    public void getBooksByCategoryId_FiveBooksWithRightCategory_ReturnsCorrectList() {
        Book firstBook = createValidBook(ID_ONE);
        BookDtoWithoutCategoryIds firstBookDto = getDtoWithoutCategoryIds(firstBook);
        when(bookMapper.toDtoWithoutCategories(firstBook)).thenReturn(firstBookDto);

        Book secondBook = createValidBook(ID_TWO);
        secondBook.setIsbn("978-1-23-456789-1");
        BookDtoWithoutCategoryIds secondBookDto = getDtoWithoutCategoryIds(secondBook);
        when(bookMapper.toDtoWithoutCategories(secondBook)).thenReturn(secondBookDto);

        Book thirdBook = createValidBook(ID_THREE);
        thirdBook.setIsbn("978-1-23-456789-2");
        BookDtoWithoutCategoryIds thirdBookDto = getDtoWithoutCategoryIds(thirdBook);
        when(bookMapper.toDtoWithoutCategories(thirdBook)).thenReturn(thirdBookDto);

        Book fourthBook = createValidBook(ID_FOUR);
        fourthBook.setIsbn("978-1-23-456789-3");
        BookDtoWithoutCategoryIds fourthBookDto = getDtoWithoutCategoryIds(fourthBook);
        when(bookMapper.toDtoWithoutCategories(fourthBook)).thenReturn(fourthBookDto);

        Book fifthBook = createValidBook(ID_FIVE);
        fifthBook.setIsbn("978-1-23-456789-4");
        BookDtoWithoutCategoryIds fifthBookDto = getDtoWithoutCategoryIds(fifthBook);
        when(bookMapper.toDtoWithoutCategories(fifthBook)).thenReturn(fifthBookDto);

        Long categoryId = ID_TWO;

        when(bookRepository.findAllByCategoryId(categoryId, STANDART_PAGEABLE))
                .thenReturn(List.of(firstBook, secondBook, thirdBook, fourthBook, fifthBook));
        
        List<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(
                categoryId, STANDART_PAGEABLE);
        
        List<BookDtoWithoutCategoryIds> expected = List.of(firstBookDto,
                secondBookDto, thirdBookDto, fourthBookDto, fifthBookDto);
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAllByCategoryId(categoryId, STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(5)).toDtoWithoutCategories(any());
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Find books by category with different categories in db")
    public void getBooksByCategoryId_BooksWithDifferentCategories_ReturnsCorrectList() {
        Book firstBook = createValidBook(ID_ONE);
        BookDtoWithoutCategoryIds firstBookDto = getDtoWithoutCategoryIds(firstBook);
        when(bookMapper.toDtoWithoutCategories(firstBook)).thenReturn(firstBookDto);

        Book secondBook = createValidBook(ID_TWO);
        secondBook.setIsbn("978-1-23-456789-1");
        secondBook.setCategories(new HashSet<>());

        Book thirdBook = createValidBook(ID_THREE);
        thirdBook.setIsbn("978-1-23-456789-2");
        BookDtoWithoutCategoryIds thirdBookDto = getDtoWithoutCategoryIds(thirdBook);
        when(bookMapper.toDtoWithoutCategories(thirdBook)).thenReturn(thirdBookDto);

        Book fourthBook = createValidBook(ID_FOUR);
        fourthBook.setIsbn("978-1-23-456789-3");
        fourthBook.setCategories(FIRST_CATEGORY_SET);

        Book fifthBook = createValidBook(ID_FIVE);
        secondBook.setIsbn("978-1-23-456789-4");
        BookDtoWithoutCategoryIds fifthBookDto = getDtoWithoutCategoryIds(fifthBook);
        when(bookMapper.toDtoWithoutCategories(fifthBook)).thenReturn(fifthBookDto);

        when(bookRepository.findAllByCategoryId(ID_TWO, STANDART_PAGEABLE))
                .thenReturn(List.of(firstBook, thirdBook, fifthBook));
        
        List<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(
                ID_TWO, STANDART_PAGEABLE);

        List<BookDtoWithoutCategoryIds> expected = List.of(firstBookDto,
                thirdBookDto, fifthBookDto);
        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAllByCategoryId(ID_TWO, STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verify(bookMapper, times(3)).toDtoWithoutCategories(any());
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to find book from books with incorrect category in db")
    public void getBooksByCategoryId_OneBookWithIncorrectCategory_ReturnsEmptyList() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();

        when(bookRepository.findAllByCategoryId(ID_ONE, STANDART_PAGEABLE))
                .thenReturn(new ArrayList<>());
        
        List<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(
                ID_ONE, STANDART_PAGEABLE);

        assertEquals(expected, actual);
        verify(bookRepository, times(ONCE)).findAllByCategoryId(ID_ONE, STANDART_PAGEABLE);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    private CreateBookRequestDto createValidCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Title");
        requestDto.setAuthor("Famous Author");
        requestDto.setIsbn("978-3-16-148410-0");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setDescription("Interesting book about journey through the Sea");
        requestDto.setCoverImage("url of book cover image with sea on it");
        requestDto.setCategoryIds(new HashSet<>(Set.of(ID_ONE)));
        return requestDto;
    }

    private Book createValidBook(Long bookId) {
        Book book = new Book(bookId);
        book.setTitle("Mysterious Title");
        book.setAuthor("Ambitious Author");
        book.setIsbn("978-1-23-456789-0");
        book.setPrice(BigDecimal.valueOf(9.85));
        book.setDescription("Detective book about murder in palace");
        book.setCoverImage("url of book cover image with blood on it");
        book.setCategories(SECOND_CATEGORY_SET);
        return book;
    }

    private Book getBookFromCreateBookRequestDto(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategories(FIRST_CATEGORY_SET);
        return book;
    }

    private BookDto getBookDtoFromBook(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return bookDto;
    }

    private Book updateBookByCreateBookRequestDtoWithoutCategories(
            Book book, CreateBookRequestDto request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setCoverImage(request.getCoverImage());
        return book;
    }

    private BookDtoWithoutCategoryIds getDtoWithoutCategoryIds(Book book) {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        return bookDto;
    }
}
