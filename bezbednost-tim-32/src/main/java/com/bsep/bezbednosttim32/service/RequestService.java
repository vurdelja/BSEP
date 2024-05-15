package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.exceptions.RequestNotFoundException;
import com.bsep.bezbednosttim32.model.Request;
import com.bsep.bezbednosttim32.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public List<Request> findAllRequests(){
        return requestRepository.findAll();
    }

    public Request updateRequest(Request request){
        return requestRepository.save(request);
    }


    public Request findRequestById(Integer id){
        return requestRepository.findRequestById(id)
                .orElseThrow(()-> new RequestNotFoundException("Request by id" + id + "not found."));
    }

    public Request addRequest(Request request){
        return requestRepository.save(request);
    }



}
