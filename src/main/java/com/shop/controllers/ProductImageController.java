package com.shop.controllers;

import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.ProductImage;
import com.shop.response.ProductImageResponse;
import com.shop.services.interfaces.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;

//    @GetMapping("")
//    public ResponseEntity<?> getAll() {
//        return ResponseEntity.ok(productImageService.getAll());
//    }

    @PostMapping(value = "/create/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductImage(@PathVariable("product_id") int productId,
                                                 @ModelAttribute("file") MultipartFile file) {
        try {
            List<ProductImageResponse> productImages = productImageService.getByProductId(productId);
            if(productImages.size() > ProductImage.MAXIMUM_IMAGES_SIZE) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large! Maximum size is 10MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }

            String filename = storeFile(file);
            ProductImageResponse productImage = productImageService.create( ProductImageDTO.builder()
                    .imageUrl(filename)
                    .productId(productId)
                    .build());

            return ResponseEntity.ok(productImage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("{id}")
//    public ResponseEntity<?> getProductImageById(@PathVariable int id) throws DataNotFoundException {
//        return ResponseEntity.ok(productImageService.getById(id));
//    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductImage(@PathVariable int id,  @ModelAttribute("file") MultipartFile file) {
        try {
            ProductImage existingProductImage = productImageService.getById(id);
            List<ProductImageResponse> productImages = productImageService.getByProductId(existingProductImage.getProduct().getId());
            if(productImages.size() > ProductImage.MAXIMUM_IMAGES_SIZE) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large! Maximum size is 10MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            String filename = storeFile(file);
            ProductImageResponse productImage = productImageService.update(ProductImageDTO.builder()
                    .imageUrl(filename)
                    .productId(existingProductImage.getProduct().getId())
                    .build(),id);
            return ResponseEntity.ok(productImage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProductImage(@PathVariable int id) throws DataNotFoundException {
        try {
            productImageService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
