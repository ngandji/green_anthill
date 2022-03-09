package com.green.DJLHelloWord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DjlHelloWordApplication{

   
    public static void main(String[] args) {
        SpringApplication.run(DjlHelloWordApplication.class, args);
    }

    /*
    public void run(String... args) throws Exception {

        Resource[] resources = new Resource[]{
            new ClassPathResource("/puppy-in-white-and-red-polka.jpg"),
            new ClassPathResource("/street-car-bus-truck.jpg"),
            new ClassPathResource("/ious-objects.png")
        };

        for ( Resource resource : resources) {
            Image image = ImageFactory.getInstance().fromInputStream(resource.getInputStream());
        }
    }

    private static void saveBoundingBoxImage(Image img, DetectedObjects detection, String fileName)
            throws IOException {
        Path outputDir = Paths.get("target/output");
        Files.createDirectories(outputDir);
    } //*/

}
