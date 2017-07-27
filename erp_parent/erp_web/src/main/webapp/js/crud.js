//提交的方法名称
var listParam = "";
var method = "";
var saveParam ="";
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:name + '_listByPage'+listParam,
		columns:columns,
		singleSelect: true,
		pagination: true,
		toolbar: [{
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				$('#editForm').form('clear');
				//设置保存按钮提交的方法为add
				method = "add";
				//关闭编辑窗口
				$('#editDlg').dialog('open');
			}
		},'-',{
			text:'导出',
			iconCls:'icon-excel',
			handler:function(){
				var formData = $('#searchForm').serializeJSON();
				$.download(name+'_export'+listParam,formData);
			}
		},'-',{
			text:'导入',
			iconCls:'icon-save',
			handler:function(){
				$('#importDlg').dialog('open');
			}
		}
		]
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	
	var _height = 200;
	if(typeof(height)!='undefined'){
		_height = height
	}
	//初始化编辑窗口
	$('#editDlg').dialog({
		title: '编辑',//窗口标题
		width: 300,//窗口宽度
		height: _height,//窗口高度
		closed: true,//窗口是是否为关闭状态, true：表示关闭
		modal: true,//模式窗口
		buttons:[{
			text:'保存',
			iconCls: 'icon-save',
			handler:function(){
				var isvalidate = $('#editForm').form('validate');
				if(!isvalidate){
					return ;
				}
				//用记输入的部门信息
				var submitData= $('#editForm').serializeJSON();
				$.ajax({
					url: name + '_' + method+saveParam,
					data: submitData,
					dataType: 'json',
					type: 'post',
					success:function(rtn){
						//{success:true, message: 操作失败}
						$.messager.alert('提示',rtn.message, 'info',function(){
							if(rtn.success){
								//关闭弹出的窗口
								$('#editDlg').dialog('close');
								//刷新表格
								$('#grid').datagrid('reload');
							}
						});
					}
				});
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				//关闭弹出的窗口
				$('#editDlg').dialog('close');
			}
		}]
	});
	//初始化编辑窗口
		$('#importDlg').dialog({
			title: '导入数据',//窗口标题
			width: 330,//窗口宽度
			height: 106,//窗口高度
			closed: true,//窗口是是否为关闭状态, true：表示关闭
			modal: true,//模式窗口
			buttons:[{
				text:'导入',
				iconCls: 'icon-save',
				handler:function(){
					var submitData = $('#importForm')[0];
					$.ajax({
						url: 'supplier_doImport',
						data: new FormData(submitData),
						dataType: 'json',
						type: 'post',
						processData:false,//如果要发送 DOM 树信息或其它不希望转换的信息，请设置为 false
		    			contentType:false,//(默认: "application/x-www-form-urlencoded") 发送信息至服务器时内容编码类型。我们上传的是字节流，不能编码
						success:function(rtn){
							//{success:true, message: 操作失败}
							$.messager.alert('提示',rtn.message, 'info',function(){
								if(rtn.success){
									$('#importDlg').dialog('close');
									$('#grid').datagrid('reload');
								}
							});
						}
					});
				}
			}]
		})
});


/**
 * 删除
 */
function del(uuid){
	$.messager.confirm("确认","确认要删除吗？",function(yes){
		if(yes){
			$.ajax({
				url: name + '_delete?id=' + uuid,
				dataType: 'json',
				type: 'post',
				success:function(rtn){
					$.messager.alert("提示",rtn.message,'info',function(){
						//刷新表格数据
						$('#grid').datagrid('reload');
					});
				}
			});
		}
	});
}

/**
 * 修改
 */
function edit(uuid){
	//弹出窗口
	$('#editDlg').dialog('open');

	//清空表单内容
	$('#editForm').form('clear');

	//设置保存按钮提交的方法为update
	method = "update";

	//加载数据
	$('#editForm').form('load',name + '_get?id=' + uuid);
}