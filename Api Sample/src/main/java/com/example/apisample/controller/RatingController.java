package com.example.apisample.controller;

import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.rating.RatingRequestDTO;
import com.example.apisample.model.dto.rating.RatingResponseDTO;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.service.Interface.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "*")
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createRating(@RequestBody @Valid RatingRequestDTO ratingRequest) throws Exception {
        log.info(LogMessage.logStartCreateRating);

        RatingResponseDTO ratingResponse = ratingService.createRating(ratingRequest);

        log.info(LogMessage.logSuccessCreateRating);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(ratingResponse)
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getRatingById(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartGetRatingById);

        RatingResponseDTO ratingResponse = ratingService.getRatingById(id);

        log.info(LogMessage.logSuccessGetRatingById);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(ratingResponse)
                        .build()
        );
    }

    @GetMapping("/get-all")
    public APIPageableResponseDTO<RatingResponseDTO> getAllRatings(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                  @RequestParam(defaultValue = "", name = "search") String search,
                                                                  @RequestParam(defaultValue = "id", name= "sort") String sort) {
        return ratingService.getAllRating(pageNo, pageSize, search, sort);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateRating(@PathVariable Integer id, @RequestBody @Valid RatingRequestDTO ratingRequest) throws Exception {
        log.info(LogMessage.logStartUpdateRating);

        ratingService.updateRating(id, ratingRequest);

        log.info(LogMessage.logSuccessUpdateRating);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteRating(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartDeleteRating);

        ratingService.deleteRating(id);

        log.info(LogMessage.logSuccessDeleteRating);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/restore/{id}")
    public ResponseEntity<ResponseObject> restoreRating(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartRestoreRating);

        ratingService.restoreRating(id);

        log.info(LogMessage.logSuccessRestoreRating);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
