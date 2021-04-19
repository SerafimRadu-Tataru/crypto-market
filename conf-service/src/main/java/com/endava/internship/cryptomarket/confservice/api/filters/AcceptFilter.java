package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.api.annotations.FilterComponent;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@FilterComponent(path = "/*", priority = 1)
public class AcceptFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String accept = req.getHeader("Content-Type");

        if ((req.getMethod().equals("POST") || req.getMethod().equals("PUT"))
                && !"application/json; charset: UTF-8".equals(accept)) {
            throw new ApplicationException(ExceptionResponses.NOT_ACCEPTABLE_CONTENT, null);
        }

        chain.doFilter(req, res);
    }

}
