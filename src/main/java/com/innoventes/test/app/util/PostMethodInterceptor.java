package com.innoventes.test.app.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innoventes.test.app.dto.CompanyDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PostMethodInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public PostMethodInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the request method is POST
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // Get the request payload (body)
            String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

            // Convert the request payload to CompanyDTO object
            CompanyDTO companyDTO = convertToCompanyDTO(requestBody);

            // Check if the webSiteURL is valid
            if (companyDTO != null && !isValidURL(companyDTO.getWebSiteURL())) {
                // If the URL is not valid, set the webSiteURL field to null
                companyDTO.setWebSiteURL(null);

                // Convert the modified CompanyDTO back to JSON
                String modifiedRequestBody = convertToJson(companyDTO);

                // Modify the request payload with the modified JSON
                request.setAttribute("modifiedRequestBody", modifiedRequestBody);
            }
        }

        return true;
    }

    private CompanyDTO convertToCompanyDTO(String requestBody) {
        try {
            return objectMapper.readValue(requestBody, CompanyDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidURL(String url) {
        // Implement your logic here to validate the URL
        return url != null && url.startsWith("http://");
    }

    private String convertToJson(CompanyDTO companyDTO) {
        try {
            return objectMapper.writeValueAsString(companyDTO);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
