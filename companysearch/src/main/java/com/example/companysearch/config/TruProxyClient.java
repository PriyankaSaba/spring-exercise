package com.example.companysearch.config;
import com.example.companysearch.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "truProxyClient", url = "https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1")
public interface TruProxyClient {

    @GetMapping("/Search")
    CompanyDto searchCompany(@RequestParam("Query") String query, @RequestParam("status") String status, @RequestHeader("x-api-key") String apiKey);

    @GetMapping("/Details")
    CompanyDto getCompanyDetails(@RequestParam("CompanyNumber") String companyNumber, @RequestHeader("x-api-key") String apiKey);
}
