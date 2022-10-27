package org.rone.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.rone.web.model.entity.User;
import org.rone.web.model.vo.Result;
import org.rone.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author rone
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(RestControllerDemo.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/getUserByNo/{userNo}", method = RequestMethod.GET)
    @ApiOperation(value = "根据用户Id获取用户信息")
    public Result<User> getUserById(@PathVariable("userNo") @ApiParam("用户唯一编号") String userNo) {
        try {
            return Result.success(userService.queryById(userNo));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用户列表")
    public Result<List<User>> getUsers() {
        try {
            return Result.success(userService.queryAllByLimit(0, 100));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }

    /**
     * @RequestBody 将整个请求体映射成一个对象，请求数据需要为json格式。该注解还会触发 RequestBodyAdvice ，如果项目里有的话。
     * @param user
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户")
    public Result<?> addUser(@RequestBody User user) {
        try {
            userService.insert(user);
            return Result.success(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    @ApiOperation(value = "更改用户")
    public Result<?> updateUser(User user) {
        try {
            return Result.success(userService.update(user));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/deleteUserByNo/{userNo}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户")
    public Result<?> deleteUser(@PathVariable String userNo) {
        try {
            return Result.success(userService.deleteById(userNo));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/getNamesByIds", method = RequestMethod.GET)
    @ApiOperation(value = "根据id集合获取姓名集合")
    public Result<List<String>> getNamesByIds(String... ids) {
        try {
            return Result.success(userService.getNamesByIds(Arrays.asList(ids)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage(), e);
        }
    }
}
