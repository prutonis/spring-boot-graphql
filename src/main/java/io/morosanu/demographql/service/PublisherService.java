package io.morosanu.demographql.service;

import io.morosanu.demographql.domain.Publisher;
import io.morosanu.demographql.gql.PublisherInput;
import io.morosanu.demographql.repo.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher createPublisher(PublisherInput publisherInput) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherInput.name());
        publisher.setAddress(publisherInput.address());
        return publisherRepository.save(publisher);
    }

    public Iterable<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }
}
