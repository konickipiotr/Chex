package com.chex.webapp.admin.places;

import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String showCategories(Model model){
        model.addAttribute("categories", this.categoryRepository.findAll());
        model.addAttribute("category", new Category());
        return "admin/places/addcategory";
    }

    @PostMapping("/new")
    public String addCategory(Category category, Model model){
        boolean eng = this.categoryRepository.existsByEng(category.getEng());
        boolean pl = this.categoryRepository.existsByPl(category.getPl());
        if(!eng && !pl)
            this.categoryRepository.save(category);
        else
            model.addAttribute("error_msg", "Category already exists");
        return "redirect:/admin/category";
    }

    @PostMapping(params = "action=EDIT")
    public String editCategory(Category category){
        this.categoryRepository.save(category);
        return "redirect:/admin/category";
    }

    @PostMapping(params = "action=DELETE")
    public String deleteCategory(Category category){
        this.categoryRepository.delete(category);
        return "redirect:/admin/category";
    }
}
