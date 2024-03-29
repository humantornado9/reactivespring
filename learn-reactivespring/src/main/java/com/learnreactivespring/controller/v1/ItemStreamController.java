package com.learnreactivespring.controller.v1;

import com.learnreactivespring.constants.ItemConstants;
import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemReactiveCappedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.PageAttributes;

@RestController
@Slf4j
public class ItemStreamController {

  @Autowired
  private ItemReactiveCappedRepository itemReactiveCappedRepository;

  @GetMapping(value = ItemConstants.ITEM_STREAM_END_POINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<ItemCapped> getItemsStreams() {
    return itemReactiveCappedRepository.findItemsBy();
  }

}
