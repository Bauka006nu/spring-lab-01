package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired MockMvc mockMvc;

    // slice tests do not scan @Service beans, so the service is replaced by a mock
    @MockitoBean BookService service;

    @Test
    @WithMockUser
    void existingBookIsReturnedAsJson() throws Exception {
        given(service.findById(1L))
                .willReturn(Optional.of(new Book(1L, "Dune", "Frank Herbert", 1965)));

        mockMvc.perform(get("/api/books/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Dune"));
    }

    @Test
    @WithMockUser
    void missingBookBecomes404() throws Exception {
        given(service.findById(7L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/7"))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void bookWithBlankTitleIsRejectedWith400() throws Exception {
        mockMvc.perform(post("/api/books")
                       .with(csrf())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"title\":\"\",\"author\":\"Frank Herbert\",\"year\":1965}"))
               .andExpect(status().isBadRequest());
    }
}
