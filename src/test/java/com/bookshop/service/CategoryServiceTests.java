package com.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookshop.dto.category.request.CategoryDtoRequest;
import com.bookshop.dto.category.response.CategoryDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.CategoryMapper;
import com.bookshop.model.Category;
import com.bookshop.repository.category.CategoryRepository;
import com.bookshop.service.impl.CategoryServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
public class CategoryServiceTests {
    private static final Long ID_ONE = 1L;
    private static final Long INCORRECT_ID = 100L;
    private static final int ONCE = 1;
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category by correct data")
    public void save_AllCorrectData_CorrectDtoReturned() {
        //given
        CategoryDtoRequest requestDto = createValidCategoryDtoRequest();
        Category expectedCategoryWithoutId = getCategoryFromCreateCategoryRequestDto(requestDto);
        Category expectedCategoryWithId = getCategoryFromCreateCategoryRequestDto(requestDto);
        expectedCategoryWithId.setId(ID_ONE);
        CategoryDto expected = getCategoryDtoFromCategory(expectedCategoryWithId);

        when(categoryMapper.toCategory(requestDto)).thenReturn(expectedCategoryWithoutId);
        when(categoryRepository.save(expectedCategoryWithoutId)).thenReturn(expectedCategoryWithId);
        when(categoryMapper.toDto(expectedCategoryWithId)).thenReturn(expected);

        //when
        CategoryDto actual = categoryService.save(requestDto);

        //then
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).save(expectedCategoryWithoutId);
        verifyNoMoreInteractions(categoryRepository);
        verify(categoryMapper, times(ONCE)).toCategory(requestDto);
        verify(categoryMapper, times(ONCE)).toDto(expectedCategoryWithId);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
        Get correct category dto\s
        when category with such id exists
            """)
    public void getById_WithValidId_CorrectDtoReturned() {
        //given
        Long categoryId = ID_ONE;
        Category category = createValidCategory(categoryId);
        CategoryDto expected = getCategoryDtoFromCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        //when
        CategoryDto actual = categoryService.getById(categoryId);

        //then
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
        verify(categoryMapper, times(ONCE)).toDto(category);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
        Verify exception thrown\s
        when category with such id doesn't exists
            """)
    public void getById_WithInValidId_ExceptionThrown() {
        //given

        when(categoryRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(INCORRECT_ID)
        );
        //then
        String expected = "Can't find category by id: " + INCORRECT_ID;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findById(INCORRECT_ID);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
        Verify the correct category dto was returned after updating\s
        when category with such id exists
            """)
    public void update_WithValidIdAndValidData_CorrectDtoReturned() {
        //given
        CategoryDtoRequest request = createValidCategoryDtoRequest();
        Long categoryId = ID_ONE;
        Category category = createValidCategory(categoryId);
        Category updatedCategory = updateCategoryByCategoryDtoRequest(category, request);
        CategoryDto expected = getCategoryDtoFromCategory(updatedCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        //when
        CategoryDto actual = categoryService.update(categoryId, request);

        //then
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findById(categoryId);
        verify(categoryRepository, times(ONCE)).save(updatedCategory);
        verifyNoMoreInteractions(categoryRepository);
        verify(categoryMapper, times(ONCE)).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
        Verify the exception will be thrown after updating\s
        when category with such id doesn't exists
            """)
    public void update_WithInValidIdAndValidData_ExceptionThrown() {
        //given
        CategoryDtoRequest request = createValidCategoryDtoRequest();
        String expected = "Can't find category by id: " + INCORRECT_ID;

        when(categoryRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        //when
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(INCORRECT_ID, request));

        //then
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findById(INCORRECT_ID);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Show list of 0 categories")
    public void findAll_NoCategoriesCorrectPageable_ReturnsEmptyList() {
        //given
        when(categoryRepository.findAll(STANDART_PAGEABLE)).thenReturn(Page.empty());

        //when
        List<CategoryDto> actual = categoryService.findAll(STANDART_PAGEABLE);

        //then
        List<CategoryDto> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findAll(STANDART_PAGEABLE);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Show list of 1 category")
    public void findAll_OneCategoryCorrectPageable_ReturnsCorrectList() {
        //given
        Category category = createValidCategory(ID_ONE);
        List<Category> categories = List.of(category);
        Page<Category> page = new PageImpl<>(categories, STANDART_PAGEABLE, categories.size());
        CategoryDto categoryDto = getCategoryDtoFromCategory(category);

        when(categoryRepository.findAll(STANDART_PAGEABLE)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        //when
        List<CategoryDto> actual = categoryService.findAll(STANDART_PAGEABLE);

        //then
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(categoryDto);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(ONCE)).findAll(STANDART_PAGEABLE);
        verifyNoMoreInteractions(categoryRepository);
        verify(categoryMapper, times(ONCE)).toDto(category);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Verify no exceptions thrown with correct id")
    public void deleteById_CorrectId_Success() {
        //given
        Long categoryId = ID_ONE;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        //when
        categoryService.deleteById(categoryId);

        //then
        verify(categoryRepository, times(ONCE)).existsById(categoryId);
        verify(categoryRepository, times(ONCE)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Verify exception thrown if category id is incorrect")
    public void deleteById_InCorrectId_ThrowsException() {
        //given
        String excepted = "Can't find category by id: " + INCORRECT_ID;
        when(categoryRepository.existsById(INCORRECT_ID)).thenReturn(false);

        //when
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(INCORRECT_ID));

        //then
        String actual = exception.getMessage();
        Assertions.assertEquals(excepted, actual);
        verify(categoryRepository, times(ONCE)).existsById(INCORRECT_ID);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    private CategoryDtoRequest createValidCategoryDtoRequest() {
        CategoryDtoRequest requestDto = new CategoryDtoRequest();
        requestDto.setName("Category Name");
        requestDto.setDescription("Interesting category about journey through the Sea");
        return requestDto;
    }

    private Category createValidCategory(Long categoryId) {
        Category category = new Category(categoryId);
        category.setName("Detective");
        category.setDescription("Detective stories");
        return category;
    }

    private Category getCategoryFromCreateCategoryRequestDto(CategoryDtoRequest requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        return category;
    }

    private CategoryDto getCategoryDtoFromCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    private Category updateCategoryByCategoryDtoRequest(
            Category category, CategoryDtoRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }
}
