package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import kz.iitu.springlab.security.BookOwnership;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService service;
    private final BookOwnership ownership;

    public BookPageController(BookService service, BookOwnership ownership) {
        this.service = service;
        this.ownership = ownership;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String author, Model model) {
        model.addAttribute("books", service.findAll(author));
        model.addAttribute("author", author);
        model.addAttribute("title", "Catalogue");
        model.addAttribute("owners", ownership.asMap());
        return "books/list";
    }

    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String author,
                         @RequestParam int year,
                         @RequestParam(required = false) String owner,
                         Principal principal,
                         RedirectAttributes redirect) {
        Book saved = service.create(new Book(null, title, author, year));
        String effectiveOwner = (owner == null || owner.isBlank()) ? principal.getName() : owner.trim();
        ownership.assign(saved.id(), effectiveOwner);
        redirect.addFlashAttribute("message", "Book «" + saved.title() + "» has been added");
        return "redirect:/books"; // 302, браузер сам делает GET /books
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable long id,
                       @RequestParam String title,
                       @RequestParam String author,
                       @RequestParam int year,
                       RedirectAttributes redirect) {
        service.update(id, new Book(null, title, author, year)).ifPresentOrElse(
                b -> redirect.addFlashAttribute("message", "Book «" + b.title() + "» has been updated"),
                () -> redirect.addFlashAttribute("message", "Book not found"));
        return "redirect:/books";
    }
}
