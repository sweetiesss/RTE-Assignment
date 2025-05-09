package com.example.apisample.rating.controller;

import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.model.dto.RatingUpdateRequestDTO;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.utils.ApiResponse;
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
@CrossOrigin(origins = "*")
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping("/products/{productId}/ratings")
    public ResponseEntity<APIPageableResponseDTO<RatingResponseDTO>> getProductRatings(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "createOn") String sort
    ) {
        return ResponseEntity.ok(ratingService.getRatingsByProductId(productId, pageNo, pageSize, sort));
    }



    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse> createRating(@RequestBody @Valid RatingRequestDTO ratingRequest) {
        log.debug(LogMessage.RATING_CREATE_START);

        ratingService.createRating(ratingRequest);

        log.info(LogMessage.RATING_CREATE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @GetMapping("/ratings/{id}")
    public ResponseEntity<ApiResponse> getRatingById(@PathVariable Integer id) {
        log.debug(LogMessage.RATING_GET_BY_ID_START);

        RatingResponseDTO ratingResponse = ratingService.getRatingById(id);

        log.info(LogMessage.RATING_GET_BY_ID_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(ratingResponse)
                        .build()
        );
    }

    @GetMapping("/ratings")
    public APIPageableResponseDTO<RatingResponseDTO> getAllRatings(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                   @RequestParam(defaultValue = "", name = "search") String search,
                                                                   @RequestParam(defaultValue = "id", name = "sort") String sort) {
        log.debug(LogMessage.RATING_GET_BY_ID_START);

        APIPageableResponseDTO<RatingResponseDTO> result = ratingService.getAllRating(pageNo, pageSize, search, sort);

        log.info(LogMessage.RATING_GET_BY_ID_SUCCESS);

        return result;
    }

    @PutMapping("/ratings/{id}")
    public ResponseEntity<ApiResponse> updateRating(@PathVariable Integer id, @RequestBody @Valid RatingUpdateRequestDTO ratingRequest) {
        log.debug(LogMessage.RATING_UPDATE_START);

        ratingService.updateRating(id, ratingRequest);

        log.info(LogMessage.RATING_UPDATE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<ApiResponse> deleteRating(@PathVariable Integer id) {
        log.debug(LogMessage.RATING_DELETE_START);

        ratingService.deleteRating(id);

        log.info(LogMessage.RATING_DELETE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/admin/ratings/restore/{id}")
    public ResponseEntity<ApiResponse> restoreRating(@PathVariable Integer id) {
        log.debug(LogMessage.RATING_RESTORE_START);

        ratingService.restoreRating(id);

        log.info(LogMessage.RATING_RESTORE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
