package io.morosanu.demographql.gql;

import io.morosanu.demographql.domain.Publisher;
import io.morosanu.demographql.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @MutationMapping("createPublisherWithInput")
    public Publisher createPublisher(@Argument PublisherInput publisher) {
        return publisherService.createPublisher(publisher);
    }

    @MutationMapping("createPublisher")
    public Publisher createPublisher(@Argument String name, @Argument String address) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setAddress(address);
        return publisherService.createPublisher(publisher);
    }

    @QueryMapping("publishers")
    public Iterable<Publisher> getPublishers() {
        return publisherService.getPublishers();
    }
}
