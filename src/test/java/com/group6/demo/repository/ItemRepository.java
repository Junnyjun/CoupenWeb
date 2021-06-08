package com.group6.demo.repository;

import com.group6.demo.entity.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static java.util.stream.IntStream.rangeClosed;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;



    @Test
    public void MakeDummies() throws Exception{
        rangeClosed(1,200).forEach(i ->{
            Item item =Item.builder()
                    .price((int) (Math.random() *15+1))
                    .stock((int) ((Math.random() *20+1)*100))
                    .thumbImg("testImg"+i)
                    .writer("writer"+i)
                    .content("testcontent"+i)
                    .title("testTitle"+i)
                    .build();

            itemRepository.save(item);
        });
    }



    @Test
    public void UpdateTest() throws Exception{
        Optional<Item> result = itemRepository.findById(300L);

        if (result.isPresent()){
            Item item = result.get();

            item.changeContent("Changed Content");
            item.changeTitle("Changed Title");

            itemRepository.save(item);
        }
     }



     @Test
     public void testQuery() throws Exception{

         Pageable pageable = PageRequest.of(0,10, Sort.by("id").descending());

         System.out.println(pageable.toString());

      }
}