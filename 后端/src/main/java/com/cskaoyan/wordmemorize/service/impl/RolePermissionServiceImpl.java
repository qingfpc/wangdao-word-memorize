package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.converter.RolePermissionConverter;
import com.cskaoyan.wordmemorize.dao.entity.PermissionDO;
import com.cskaoyan.wordmemorize.dao.entity.RoleDO;
import com.cskaoyan.wordmemorize.dao.entity.RolePermissionDO;
import com.cskaoyan.wordmemorize.dao.mapper.PermissionMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RoleMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RolePermissionMapper;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.dev33.satoken.SaManager.log;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RolePermissionMapper rolePermissionMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    RolePermissionConverter rolePermissionConverter;



    @Override
    public void savePermissions(PermissionCommand command) {
        if (command==null){
            throw new BusinessException(ResultCodeEnum.PERMISSION);
        }
        String idStr = command.getId();
        if (idStr==null||idStr.trim().isEmpty()){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!idStr.matches("\\d+")) { // 简单校验是否为数字
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        PermissionDO permissionDO = rolePermissionConverter.command2PermissionDO(command);
        permissionDO.setId(Long.parseLong(command.getId()));
        permissionMapper.insert(permissionDO);
    }

    @Override
    public List<PermissionDTO> getPermissions() {

        // 1. 查询所有权限数据（日志确认有79条）
        List<PermissionDO> allPermissions = permissionMapper.selectList(null);
        log.info("查询到的权限总数：{}", allPermissions.size());

        // 2. 直接打印前3条数据的关键信息（排查数据是否正常）
        for (int i = 0; i < Math.min(3, allPermissions.size()); i++) {
            PermissionDO temp = allPermissions.get(i);
            log.info("权限样本 {}: id={}, parentPermission={}, name={}",
                    i+1, temp.getId(), temp.getParentPermission(), temp.getName());
        }

        // 3. 构建权限树（使用更宽松的根节点条件）
        List<PermissionDTO> permissionTree = buildPermissionTree(allPermissions, null);
        log.info("最终构建的权限树节点数：{}", permissionTree.size());

        // 4. 如果构建结果为空，尝试返回所有权限（非树状，用于测试）
        if (permissionTree.isEmpty()) {
            log.warn("权限树构建为空，返回平级权限列表");
            List<PermissionDTO> flatList = allPermissions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
// 确保至少返回原始数据
            return flatList;
        }
        return permissionTree;
    }

    /**
     * 递归构建权限树（优化根节点筛选逻辑）
     */
    private List<PermissionDTO> buildPermissionTree(List<PermissionDO> allPermissions, Long parentId) {
        List<PermissionDTO> childPermissions = new ArrayList<>();

        for (PermissionDO permissionDO : allPermissions) {
            Long currentParentId = permissionDO.getParentPermission();

            // 宽松的根节点判断：parentId为null时，匹配parentPermission为null、0或空（兼容各种可能）
            boolean isChild;
            if (parentId == null) {
                isChild = currentParentId == null
                        || currentParentId == 0
                        || (currentParentId.toString().trim().isEmpty());
            } else {
                // 子节点判断：严格匹配parentId
                isChild = parentId.equals(currentParentId);
            }

            if (isChild) {
                PermissionDTO dto = convertToDTO(permissionDO);
                // 递归查询子权限
                dto.setChildren(buildPermissionTree(allPermissions, permissionDO.getId()));
                childPermissions.add(dto);
                log.debug("添加子权限：{}（父ID：{}）", dto.getName(), parentId);
            }
        }

        return childPermissions;
    }
    private PermissionDTO convertToDTO(PermissionDO doObj) {
        PermissionDTO dto = new PermissionDTO();
        // 强制赋值关键字段，避免遗漏
        dto.setId(doObj.getId() != null ? doObj.getId().toString() : "");
        dto.setParentPermission(doObj.getParentPermission() != null ? doObj.getParentPermission().toString() : "");
        dto.setName(doObj.getName() != null ? doObj.getName() : "");
        dto.setPermissionAlias(doObj.getPermissionAlias() != null ? doObj.getPermissionAlias() : "");
        dto.setPermissionType(doObj.getPermissionType() != null ? doObj.getPermissionType() : 0);
        dto.setPermissionCode(doObj.getPermissionCode() != null ? doObj.getPermissionCode() : "");
        dto.setDesc(doObj.getDesc() != null ? doObj.getDesc() : "");
        dto.setPath(doObj.getPath() != null ? doObj.getPath() : "");
        dto.setAccessType(doObj.getAccessType() != null ? doObj.getAccessType() : "");
        return dto;
    }



    @Override
    public DeletePermissionOperateDTO deletePermission(Long permissionId) {
//        初始化返回日志
        DeletePermissionOperateDTO logDTO = new DeletePermissionOperateDTO();
//        记录传入的日志
        logDTO.setId(permissionId);
//        查询权限信息
        PermissionDO permissionDO = permissionMapper.selectById(permissionId);
//        处理权限不存在的情况,返回记录了传入的ID的信息
        if (permissionDO==null){
            return logDTO;
        }
//       权限存在时,填充DTO的权限日志信息
        logDTO.setName(permissionDO.getName());
        logDTO.setPermissionType(String.valueOf(permissionDO.getPermissionType()));
        logDTO.setPermissionAlias(permissionDO.getPermissionAlias());
//执行删除操作
        int affectedRows = permissionMapper.deleteById(permissionId);

        System.out.println("affectedRows = " + affectedRows);

        return logDTO;
    }

    @Override
    public void updatePermission(PermissionCommand command) {
        if (command==null){
            throw new IllegalArgumentException("权限信息不能为空");
        }

        PermissionDO permissionDO = rolePermissionConverter.command2PermissionDO(command);
        permissionDO.setId(Long.parseLong(command.getId()));

        UpdateWrapper<PermissionDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",permissionDO.getId());
        permissionMapper.update(permissionDO,updateWrapper);
    }

    //获取账号角色列表
    @Override
    public PageDTO<RoleDTO> getRoleList(PageRequest pageRequest) {
        //不需要分页的情况
        if(pageRequest.getPageNum()==null){
            List<RoleDO> roleDOS = roleMapper.selectList(null);
            return rolePermissionConverter.roleList2Page(roleDOS, (long) roleDOS.size());
        }
        //需要分页
        //创建分页对象
        Page<RoleDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        //构造查询条件，如果关键字非空，模糊查询角色名
        QueryWrapper<RoleDO> wrapper = new QueryWrapper<>();
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().trim().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword().trim());
        }
        //执行分页查询
        Page<RoleDO> roleDOPage = roleMapper.selectPage(page, wrapper);
        //利用转换器将实体转换为DTO分页结果
        return rolePermissionConverter.roleList2Page(roleDOPage.getRecords(), roleDOPage.getTotal());
    }


    //添加角色
    @Override
    public void addRole(RoleCommand command) {
        RoleDO roleDO = rolePermissionConverter.command2RoleDO(command);
        roleMapper.insert(roleDO);
    }

    //修改账号角色
    @Override
    public void updateRole(RoleCommand roleCommand) {
        RoleDO roleDO = rolePermissionConverter.command2RoleDO(roleCommand);
        roleMapper.updateById(roleDO);
    }

    //账号角色授权
    @Override
    public void updateRolePermission(RolePermissionCommand command) {
        String roleIdStr = command.getRoleId();
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        Long roleId = Long.valueOf(roleIdStr);

        List<Long> permissionIds = Optional.ofNullable(command.getPermissions())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();

        // 1. 逻辑删除旧权限（设置为 is_deleted = 1）
        LambdaUpdateWrapper<RolePermissionDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermissionDO::getRoleId, roleId)
                .eq(RolePermissionDO::getIsDeleted, 0)
                .set(RolePermissionDO::getIsDeleted, 1);
        rolePermissionMapper.update(null, updateWrapper);

        // 2. 批量插入新权限（确保真正插入数据库）
        if (!permissionIds.isEmpty()) {
            List<RolePermissionDO> newRelations = permissionIds.stream()
                    .map(pid -> {
                        RolePermissionDO rp = new RolePermissionDO();
                        rp.setRoleId(roleId);
                        rp.setPermissionId(pid);
                        rp.setIsDeleted(0);
                        return rp;
                    })
                    .collect(Collectors.toList());

            // 使用批量插入方法，确保数据真正入库
            for (RolePermissionDO relation : newRelations) {
                rolePermissionMapper.insert(relation);
            }
        }
    }


    //获取角色对应的授权信息(授权树)
    @Override
    public RolePermissionDTO getRolePermissionList(Long roleId) {
        /*
            1. 查询所有未逻辑删除的权限数据
            这里拿到系统中所有权限的完整列表，用于构建权限树
         */

        List<PermissionDO> allPermissions = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .eq(PermissionDO::getIsDeleted, 0)
        );

        // 2. 查询指定角色已分配的权限 ID 列表
        //    过滤掉逻辑删除的关联，只取有效权限ID
        List<Long> assignedPermissionIds = rolePermissionMapper.selectList(
                        new LambdaQueryWrapper<RolePermissionDO>()
                                .eq(RolePermissionDO::getRoleId, roleId)
                                .eq(RolePermissionDO::getIsDeleted, 0)
                ).stream()
                .map(RolePermissionDO::getPermissionId)  // 取权限ID字段
                .collect(Collectors.toList());

        // 3. 构建权限树
        //    使用Map<Long, PermissionDTO>缓存所有权限的DTO，便于快速查找和构建树结构
        Map<Long, PermissionDTO> map = new HashMap<>();
        //    roots存储顶层权限节点（即没有父权限或父权限为0/-1的权限）
        List<PermissionDTO> roots = new ArrayList<>();

        // 3.1 将所有权限DO转换为DTO，并放入map缓存
        for (PermissionDO permissionDO : allPermissions) {
            PermissionDTO dto = new PermissionDTO();
            // 复制属性，减少手动赋值错误
            BeanUtils.copyProperties(permissionDO, dto);
            // id和parentPermission字段转换成String类型，方便前端使用
            dto.setId(String.valueOf(permissionDO.getId()));
            dto.setParentPermission(permissionDO.getParentPermission() == null
                    ? null : String.valueOf(permissionDO.getParentPermission()));
            // 初始化子节点列表，保证不会出现空指针异常
            dto.setChildren(new ArrayList<>());
            // 将dto存入map，key为权限ID，方便后续通过父id查找对应权限
            map.put(permissionDO.getId(), dto);

            // 调试用，输出当前权限及其父权限id
            System.out.println("Permission id=" + permissionDO.getId() + ", parentPermission=" + permissionDO.getParentPermission());
        }

        // 3.2 构建树结构：
        //     遍历所有权限DTO，判断其父权限，
        //     如果是顶层权限，直接加入roots列表，
        //     否则找到其父节点，在父节点的children列表中添加当前权限
        for (PermissionDTO dto : map.values()) {
            if (dto.getParentPermission() == null
                    || dto.getParentPermission().equals("0")
                    || dto.getParentPermission().equals("-1")) {
                // 没有父权限或者父权限id为0/-1表示这是顶层权限
                roots.add(dto);
            } else {
                // 有父权限，尝试从map中找到对应父权限DTO
                PermissionDTO parent = map.get(Long.valueOf(dto.getParentPermission()));
                if (parent != null) {
                    // 父权限存在，将当前权限添加到父权限的子权限列表
                    parent.getChildren().add(dto);
                } else {
                    // 如果找不到父权限，输出警告，方便排查数据异常
                    System.out.println("Parent not found for permission id=" + dto.getId());
                }
            }
        }

        // 调试：打印顶层权限节点数
//        System.out.println("roots size = " + roots.size());

        // 4. 封装返回结果
        RolePermissionDTO dto = new RolePermissionDTO();
        // 角色可用的系统权限树，供前端渲染菜单或权限列表
        dto.setSystemPermissions(roots);
        // 角色已分配权限ID列表，转换为字符串，方便前端高亮或勾选权限
        dto.setAssignedPermissions(assignedPermissionIds.stream()
                .map(String::valueOf)
                .collect(Collectors.toList()));

        // 返回完整的权限数据传输对象
        return dto;
    }


    //删除账号角色
    @Override
    public DeleteRoleOperateDTO deleteRole(Long roleId) {
        //转换需要返回的内容
        DeleteRoleOperateDTO result = new DeleteRoleOperateDTO();
        //真正的执行删除账号角色业务
        roleMapper.deleteById(roleId);
        return result;
    }

    @Override
    public DeleteRolePermissionOperateDTO deleteRolePermission(Long roleId, Long permissionId) {
        // 1. 检查角色和权限是否存在
        RoleDO role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }

        PermissionDO permission = permissionMapper.selectById(permissionId);
        if (permission == null) {
            throw new IllegalArgumentException("权限不存在");
        }

        // 2. 逻辑删除角色权限关系
        LambdaUpdateWrapper<RolePermissionDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermissionDO::getRoleId, roleId)
                .eq(RolePermissionDO::getPermissionId, permissionId)
                .eq(RolePermissionDO::getIsDeleted, 0)
                .set(RolePermissionDO::getIsDeleted, 1);

        int affectedRows = rolePermissionMapper.update(null, updateWrapper);
        if (affectedRows == 0) {
            throw new IllegalStateException("未找到对应的角色权限关系或已被删除");
        }

        // 3. 构建返回对象
        DeleteRolePermissionOperateDTO dto = new DeleteRolePermissionOperateDTO();
        dto.setRoleId(roleId);
        dto.setPermissionId(permissionId);
        dto.setRoleName(role.getName());
        dto.setPermissionAlias(permission.getPermissionAlias());

        return dto;
    }


    @Override
    public boolean saveBatch(Collection<RolePermissionDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<RolePermissionDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<RolePermissionDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(RolePermissionDO entity) {
        return false;
    }

    @Override
    public RolePermissionDO getOne(Wrapper<RolePermissionDO> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Optional<RolePermissionDO> getOneOpt(Wrapper<RolePermissionDO> queryWrapper, boolean throwEx) {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getMap(Wrapper<RolePermissionDO> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<RolePermissionDO> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<RolePermissionDO> getBaseMapper() {
        return null;
    }

    @Override
    public Class<RolePermissionDO> getEntityClass() {
        return null;
    }
}
