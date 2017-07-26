$(function(){
	//加载表格数据
	$('#grid').datagrid({
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
	$("#btnSearch").bind('click', function() {
		var sumbitData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',sumbitData);
	});
});