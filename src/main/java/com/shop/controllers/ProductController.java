package com.shop.controllers;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.dtos.ProductDTO;
import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Product;
import com.shop.models.ProductImage;
import com.shop.response.ProductImageResponse;
import com.shop.response.ProductListResponse;
import com.shop.response.ProductResponse;
import com.shop.services.interfaces.IProductRedisService;
import com.shop.services.interfaces.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import com.cloudinary.utils.*;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final Cloudinary cloudinary;
    private final IProductRedisService productRedisService;
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) throws DataNotFoundException {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            ProductResponse createdProduct = productService.create(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) throws DataNotFoundException {
        try {

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        ProductResponse productResponse = productService.getProductDetailById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllSearchProducts(@RequestParam(value = "cateId", required = false,defaultValue = "0") Integer cateId,
                                                        @RequestParam(value = "keyword", required = false,defaultValue = "") String keyword,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) throws JsonProcessingException {
        int totalPage = 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        ProductListResponse productListResponses = productRedisService.getAllProducts(cateId, keyword, pageRequest);
        if(productListResponses == null) {
            Page<ProductResponse> products = productService.searchProducts(cateId, keyword, pageRequest);
            totalPage = products.getTotalPages();
            productListResponses = ProductListResponse.builder()
                    .products(products.getContent())
                    .totalPages(totalPage)
                    .build();
            productRedisService.saveAllProducts(productListResponses,cateId,keyword,pageRequest);
        }


        return ResponseEntity.ok(productListResponses);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @Valid @RequestBody ProductDTO productDTO) throws DataNotFoundException {
        try {
            ProductResponse updatedProduct = productService.update(productDTO, id);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) throws DataNotFoundException {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/by-ids")
    public ResponseEntity<?> getProductDetailsByIds(@RequestParam(name = "ids") String ids) {
        try {
            List<Integer> productIds = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
            List<ProductResponse> products = productService.getProductDetailsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") int productId,
            @ModelAttribute("files") List<MultipartFile> files) {
        try {
            List<ProductImage> productImages = new ArrayList<>();
            Product existingProduct = productService.getById(productId);
            files = files == null ? new ArrayList<>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_SIZE) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            for (MultipartFile file :files) {
                if(file != null) {
                    if(file.getSize() == 0) continue;
                    if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body("File is too large! Maximum size is 10MB");
                    }
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("File must be an image");
                    }
                    String filename = saveToCloudinary(file);
                    ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder()
                            .imageUrl(filename).build());
                    productImages.add(productImage);
                }
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String saveToCloudinary(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        Map params = ObjectUtils.asMap(
                "folder", "fashion-shop-images",
                "resource_type", "image"
        );
        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return (String) result.get("secure_url");
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if(urlResource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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

