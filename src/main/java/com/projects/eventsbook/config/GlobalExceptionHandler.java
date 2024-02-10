package com.projects.eventsbook.config;


import com.google.zxing.qrcode.decoder.Mode;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidOperationException.class)
    public String handleInvalidOperationException(Exception ex, RedirectAttributes model) {
        model.addFlashAttribute("error", ex.getMessage());

        return "redirect:/profile/error";
    }

//    @ExceptionHandler(Exception.class)
//    public String handleGenericException(Exception ex, RedirectAttributes model) {
//        model.addFlashAttribute("error", "Internal Server Error (Custom)");
//
//        return "redirect:/profile/error";
//    }

}
