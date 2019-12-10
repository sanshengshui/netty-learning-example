package com.sanshengshui.webflux.controller;

import com.sanshengshui.webflux.vo.UserVO;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james mu
 * @date 2019/12/10 18:02
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/list")
    public Flux<UserVO> list() {
        List<UserVO> result = new ArrayList<>();
        UserVO userVO = new UserVO();
        userVO.setId(1);
        userVO.setUsername("mushuwei");
        result.add(userVO);

        UserVO userVO1 = new UserVO();
        userVO1.setId(2);
        userVO1.setUsername("sanshengshui");
        result.add(userVO1);

        UserVO userVO2 = new UserVO();
        userVO2.setId(3);
        userVO2.setUsername("jamesmsw");
        result.add(userVO2);

        return Flux.fromIterable(result);
    }

    @GetMapping("/get")
    public Mono<UserVO> get(@RequestParam("id") Integer id) {
        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO.setUsername("mushuwei");
        return Mono.just(userVO);
    }

    @PostMapping("add")
    public Mono<Integer> add(@RequestBody Publisher<UserVO> addVO) {
        Integer returunId = 1;
        return Mono.just(returunId);
    }

    @PostMapping("/update")
    public Mono<Boolean> update(@RequestBody Publisher<UserVO> updateVO) {
        Boolean success = true;
        return Mono.just(success);
    }

    @PostMapping("/delete")
    public Mono<Boolean> delete(@RequestParam("id") Integer id) {
        Boolean success = false;
        return Mono.just(success);
    }
}
