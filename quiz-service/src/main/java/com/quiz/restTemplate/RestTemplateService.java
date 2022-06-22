package com.quiz.restTemplate;

import com.quiz.Dto.AccountDto;
import com.quiz.service.Decode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class RestTemplateService {
    @Value("${service.account.api.host}")
    private String libraryServiceHost;


    @Autowired
    private Decode decode;

    private final String subpass = "/{id}";
    private final String detailUser = "/getuserbyusername/{name}";
    private final String canRead = "/canread/{username}/{perid}";
    private final String canUpdate = "/canupdate/{username}/{perid}";
    private final String canCreate = "/cancreate/{username}/{perid}";
    private final String listUserId = "/listuserid/{name}";

    @Autowired
    RestTemplate restTemplate;

    public AccountDto getName(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);

        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);

        AccountDto user = restTemplate.getForObject(uriBuilder.toUriString() + subpass, AccountDto.class, params);
        return user;
    }

    public AccountDto getDetailUser(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        AccountDto user = restTemplate.getForObject(uriBuilder.toUriString() + detailUser, AccountDto.class, params);
        return user;
    }

    public List<Long> getListUserId(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        List<Long> list = restTemplate.getForObject(uriBuilder.toUriString() + listUserId, List.class, params);
        return list;
    }

    public boolean getCanRead(long per, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);
        String username = decode.getUsername(token);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("perid", per + "");
        boolean canread = restTemplate.getForObject(uriBuilder.toUriString() + canRead, Boolean.class, params);
        return canread;
    }

    public boolean getCanUpdate(long per, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);
        String username = decode.getUsername(token);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("perid", per + "");
        boolean canupdate = restTemplate.getForObject(uriBuilder.toUriString() + canUpdate, Boolean.class, params);
        return canupdate;
    }

    public boolean getCanCreate(long per, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);
        String username = decode.getUsername(token);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("perid", per + "");
        boolean cancreate = restTemplate.getForObject(uriBuilder.toUriString() + canCreate, Boolean.class, params);
        return cancreate;
    }
}
