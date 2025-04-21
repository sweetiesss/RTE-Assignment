package com.example.apisample.rating.controller;

import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.utils.ResponseObject;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "*")
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @PostMapping()
    public ResponseEntity<ResponseObject> createRating(@RequestBody @Valid RatingRequestDTO ratingRequest) {
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getRatingById(@PathVariable Integer id) {
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

    @GetMapping()
    public APIPageableResponseDTO<RatingResponseDTO> getAllRatings(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                  @RequestParam(defaultValue = "", name = "search") String search,
                                                                  @RequestParam(defaultValue = "id", name= "sort") String sort) {
        return ratingService.getAllRating(pageNo, pageSize, search, sort);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateRating(@PathVariable Integer id, @RequestBody @Valid RatingRequestDTO ratingRequest) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteRating(@PathVariable Integer id) {
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

    @PostMapping("/admin/restore/{id}")
    public ResponseEntity<ResponseObject> restoreRating(@PathVariable Integer id) {
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
