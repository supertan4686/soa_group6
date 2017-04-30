package com.shareservice.Controller;

import com.shareservice.Adapter.TokenAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by super on 21/3/2560.
 */

@CrossOrigin
@Controller
@RestController
public class ShareDocumentController {

    private final ShareDocumentRepository shareDocumentRepository;

    @Autowired
    public ShareDocumentController(ShareDocumentRepository shareDocumentRepository) {
        this.shareDocumentRepository = shareDocumentRepository;
    }

    @DeleteMapping("/documents/{documentId}/shares")
    public @ResponseBody
    Map<String, Object> revokeDepartmentFromDoc (@PathVariable String documentId,
                                                 @RequestBody Map<String, Integer> departmentId,
                                                 @RequestHeader("Authorization") String authorization){
        //split ("Bearer ") and get token
        String[] authorizationSplit = authorization.split("Bearer ");
        String token = authorizationSplit[1];

        //Declare status
        Map<String, Object> resource = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try{
            Map<String, Object> data = new TokenAdapter().getDataByToken(token);
            if(checkToken(data)){
                Map<String, Object> status = shareDocumentRepository.revokeDepartmentFromDoc(Integer.parseInt(documentId), departmentId.get("departmentId"));
                return status;
            }
            else {
                result.put("error", resource);
                resource.put("message", "Invalid Token");
                return result;
            }
        }
        catch (Exception e){
            //return error status
            result.put("error", resource);
            resource.put("message", "Invalid Token");
            return result;
        }
    }

    @PostMapping("/documents/{documentId}/shares")
    public @ResponseBody
    Map<String, Object> addDepartmentToDoc (@PathVariable String documentId,
                                            @RequestBody Map<String, Integer> departmentId,
                                            @RequestHeader("Authorization") String authorization){
        //split ("Bearer ") and get token
        String[] authorizationSplit = authorization.split("Bearer ");
        String token = authorizationSplit[1];

        //Declare status
        Map<String, Object> resource = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try{
            Map<String, Object> data = new TokenAdapter().getDataByToken(token);
            if(checkToken(data)){
                Map<String, Object> status = shareDocumentRepository.postShareToOtherDepartment(Integer.parseInt(documentId), departmentId.get("departmentId"));
                return status;
            }
            else {
                result.put("error", resource);
                resource.put("message", "Invalid Token");
                return result;
            }
        }catch (Exception e){
            result.put("error", resource);
            resource.put("message", "Invalid Token");
            return result;
        }

    }

    @GetMapping("/documents/{documentId}/shares")
    public @ResponseBody
    List<Map<String, Object>> getListDepartmentStatusExceptOwnerByDoc
            (@PathVariable String documentId, @RequestHeader("Authorization") String authorization){

        //split ("Bearer ") and get token
        String[] authorizationSplit = authorization.split("Bearer ");
        String token = authorizationSplit[1];

        try {
            Map<String, Object> data = new TokenAdapter().getDataByToken(token);
            if(checkToken(data)){
                System.out.println("check token pass");
                List<Map<String, Object>> listDepartmentWithStatusByDoc = shareDocumentRepository.getListDepartmentWithStatusExceptOwnerByDoc(Integer.parseInt(documentId));
                return listDepartmentWithStatusByDoc;
            }
            else{
                System.out.println("check token fail");
                return new ArrayList<>();
            }
        }catch (Exception e){
            System.out.println("check token fail (catch)");
            return new ArrayList<>();
        }
    }

    public Boolean checkToken(Map<String, Object> data){
        if (data.get("iss").toString().equals("DocuPool")){
            return true;
        }
        else{
            return false;
        }
    }
}

