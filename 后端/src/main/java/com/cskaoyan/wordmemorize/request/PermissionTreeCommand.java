package com.cskaoyan.wordmemorize.request;

import lombok.Data;

import java.util.List;

@Data
public class PermissionTreeCommand {

    List<PermissionCommand> permissions;
}
