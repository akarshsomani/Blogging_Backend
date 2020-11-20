package com.demo.Blog.util;

import com.demo.Blog.models.ArticleEntity;
import lombok.extern.log4j.Log4j2;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Log4j2
public class Slug {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String makeSlug(String content) {
        String noWhitespace = WHITESPACE.matcher(content).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String noLatin = NONLATIN.matcher(normalized).replaceAll("");
        String slug = noLatin.toLowerCase(Locale.ENGLISH);
        return slug;
    }

//    public Long getId(String slug) {
//
//        int iend = slug.indexOf("-"); //this finds the first occurrence of "-"
//
//        String subString = "";
//        if (iend != -1)
//        {
//            subString= slug.substring(0 , iend);
//            return Long.parseLong(subString);
//        }
//        log.info("Invalid slug - {}", slug);
//        return null;
//    }
}
