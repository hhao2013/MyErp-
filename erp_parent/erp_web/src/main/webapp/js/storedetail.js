$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:'storedetail_listByPage',
		columns:[[
  		    {field:'uuid',title:'编号',width:100},
  		    {field:'storeName',title:'仓库',width:100},
  		    {field:'goodsName',title:'商品',width:100},
  		    {field:'num',title:'数量',width:100}
			]],
		singleSelect: true,
		pagination: true,
		onDblClickRow:function(rowIndex, rowData){
			/*
			 在用户双击一行的时候触发，参数包括：
			rowIndex：点击的行的索引值，该索引值从0开始。
			rowData：对应于点击行的记录。
			*/
			$('#storeoperDlg').dialog('open');
			$('#storeName').html(rowData.storeName);
			$('#goodsName').html(rowData.goodsName);
			//加载明细的数据
			$('#storeopergrid').datagrid('load',{
					"t1.storeuuid":rowData.storeuuid,
					"t1.goodsuuid":rowData.goodsuuid
			});
		}
	});
	//明细表格
	$('#storeopergrid').datagrid({
		url:'storeoper_listByPage',
		columns:[[
  		    {field:'uuid',title:'编号',width:100},
  		    {field:'empName',title:'操作员工',width:100},
  		    {field:'opertime',title:'操作日期',width:100,formatter:function(value){
  		    	return new Date(value).Format("yyyy-MM-dd");
  		    }},
  		    {field:'storeName',title:'仓库',width:100},
  		    {field:'goodsName',title:'商品',width:100},
  		    {field:'num',title:'数量',width:100},
  		    {field:'type',title:'操作类型',width:100,formatter:function(value){
  		    	if(value*1 == 1){
  		    		return "入库";
  		    	}
  		    	if(value*1 == 2){
  		    		return "出库";
  		    	}
  		    }},
			]],
		singleSelect: true,
		pagination: true
	});
	$('#storeoperDlg').dialog({
		width:710,
		height:400,
		title:'库存变动查询',
		modal:true,
		closed:true
	});
/*	
	$('#itemDlg').dialog({
		title:dlgTitle,
		width:300,
		height:200,
		modal:true,
		closed:true,
		buttons:[
		    {
		    	text:dlgTitle,
		    	iconCls:'icon-save',
		    	handler:doInOutStore
		    }
		]
	});
	*/
	$('#btnSearch').bind('click',function(){
		var submitData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',submitData);
	});
});
