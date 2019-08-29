package com.example.product;

import com.example.product.domain.Product;
import com.example.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.restdocs.restassured3.operation.preprocess.RestAssuredPreprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

/**
 * @author mukibul
 * @since 27/08/19
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(value = {"eureka.client.enabled=false","zipkin.client.enable=false"},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductApiDocumentation {
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Getter
    @Setter
    private RestOperations restTemplate	= new RestTemplate();

//    @Value("http://localhost:${local.server.port}")
//    protected String host;

    @Autowired
    private ObjectMapper objectMapper;



    @Autowired
    private ProductRepository productRepository;

    @LocalServerPort
    private int port;


//    @Autowired
//    private ProductEventSink pipe;

    @Autowired
    private MessageCollector messageCollector;

    private RestDocumentationFilter documentationFilter;

    private RequestSpecification spec;



    @Before
    public void setUp() {

        this.documentationFilter = document("{method-name}",
                Preprocessors.preprocessRequest(RestAssuredPreprocessors.modifyUris()
                        .host("172.27.80.77")
                        .removePort()));

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(this.restDocumentation))
                .addFilter(this.documentationFilter).build();
    }


    //Ignore self and profile links in documentation
    public static LinksSnippet links(LinkDescriptor... descriptors) {
        return HypermediaDocumentation.links(halLinks(),descriptors).and(linkWithRel("self").ignored().optional(),
                linkWithRel("curies").ignored().optional(),linkWithRel("profile").ignored().optional());
    }

    //Ignore all _links fields in documentation
    public static ResponseFieldsSnippet responseFields(FieldDescriptor... fieldDescriptors){
        List<FieldDescriptor> filteredDescriptors = new ArrayList<>(0);
        for(FieldDescriptor descriptor: fieldDescriptors){
            if(descriptor.getPath().contains("_links")){
                descriptor.ignored();
            }
            if(descriptor.getPath().contains("version")){
                descriptor.ignored();
            }
            filteredDescriptors.add(descriptor);
        }

        return PayloadDocumentation.responseFields(filteredDescriptors);
    }



    @Test
    public void addProduct() throws JsonProcessingException {
        Map<String,Object> productMap = new HashMap();
        productMap.put("name","Adidas shoe size 8");
        productMap.put("price",2000.00);

        RestAssured.given(this.spec)
                .accept("application/json")
                .filter(this.documentationFilter.document(
                        requestFields(
                                fieldWithPath("name").description("Name of the product"),
                                fieldWithPath("price").description("Price of the product")
                        ),
                        responseFields(
                                fieldWithPath("name").description("Name of the product"),
                                fieldWithPath("price").description("Price of the product"),
                                fieldWithPath("_links.self.href").description("<<product-links,Links>> to this resource"))
                ))
                .body(this.objectMapper.writeValueAsString(productMap)).contentType("application/json")
                .when().port(this.port).post("/products")
                .then()
                .assertThat().statusCode(201)
                .log().body();
    }

    @Test
    public void addProductToCart() {
        Product product = new Product("Test obj",2000.0);
        product = productRepository.save(product);
        //ProductAddToCartEvent.Product product1 = new ProductAddToCartEvent.Product(product.getId(),"Test obj","2000.0");

//        ProductAddToCartEvent productAddToCartEvent =new ProductAddToCartEvent(product1,1L);
//        boolean send = pipe.productEventsSubscriberChannel()
//                .send(MessageBuilder.withPayload(productAddToCartEvent)
//                        .build());

        RestAssured.given(this.spec)
                .accept("application/json")
                .filter(this.documentationFilter.document(
                        pathParameters(
                                parameterWithName("productId").description("Product id"),
                                parameterWithName("cartId").description("Shopping cart id")
                        )
                ))
                .when().port(this.port).put("/products/{productId}/addToCart/{cartId}",product.getId(),1)
                .then()
                .assertThat().statusCode(200)
                .log().body();
    }


}
