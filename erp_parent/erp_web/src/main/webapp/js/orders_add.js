var exsitEditindex = -1;
$(function(){
	$('#ordersgrid').datagrid({
		singleSelect:true,
		showFooter:true,
	    columns:[[    
	    	{field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{disabled:true}}},
  		    {field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
  		    	url:'goods_list',    
  		    	valueField:'name',    
  		    	textField:'name',
  		    	onSelect:function(goods){
  		    		var goodUuidEditor = getEditor('goodsuuid');
  		    		$(goodUuidEditor.target).val(goods.uuid);
  		    		var goodPriceEditor = getEditor('price');
  		    		if(types==1){
	  		    		$(goodPriceEditor.target).val(goods.inprice);
  		    		}
  		    		if(types==2){
  		    			$(goodPriceEditor.target).val(goods.outprice);
  		    		}
  		    		var numEditor = getEditor('num');
  		    		$(numEditor.target).select();
  		    		bindGridEvent();
  		    		cal();
  		    		sum();
  		    	}
  		    }}},
  		    {field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{min:0,precision:2}}},
  		    {field:'num',title:'数量',width:100,editor:{type:'numberbox'}},
  		    {field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{min:0,precision:2,disabled:true}}},
			{field:'-',title:'操作',width:200,formatter: function(value,row,index){
				if(row.num=='合计'){
					return "";
				}
				return ' <a href="javascript:void(0)" onclick="deleteRow(' + index + ')">删除</a>';
			}}
	    ]],
	    toolbar: [{
	    	text:'增加',
			iconCls: 'icon-add',
			handler: function(){
				if(exsitEditindex>-1){
					$('#ordersgrid').datagrid('endEdit',exsitEditindex);
				};
				$('#ordersgrid').datagrid('appendRow',{
					num:0,
					money:0
				});
				exsitEditindex = $('#ordersgrid').datagrid('getRows').length -1 ; 
				$('#ordersgrid').datagrid('beginEdit',exsitEditindex);
			}
		},'-',{
			text:'提交',
			iconCls: 'icon-save',
			handler: function(){
				if(exsitEditindex>-1){
					$('#ordersgrid').datagrid('endEdit',exsitEditindex);
				};
				var datasubmit = $('#orderForm').serializeJSON();
				if(datasubmit['t.supplieruuid']==""){
					$.messager.alert("提示","请选择供应商","info");
					return ;
				}
				var rows = $('#ordersgrid').datagrid('getRows');
				datasubmit.json = JSON.stringify(rows);
				$.ajax({
					url:'orders_add?t.type='+types,
					data:datasubmit,
					dataType:'json',
					type:'post',
					success:function(rtn){
						$.messager.alert('',rtn.message,'info',function(){
							if(rtn.success){
								$('#supplier').combogrid('clear');
								$('#ordersgrid').datagrid('loadData',{total:0,rows:[],footer:[{num:'合计',money:0}]});
								$('#addordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							}else{
								location.href="login.html";
							}
						});
					}
				});
			}
		}],
		onClickRow:function(rowIndex, rowData){
			if(exsitEditindex>-1){
				$('#ordersgrid').datagrid('endEdit',exsitEditindex);
			};
			exsitEditindex = rowIndex;
			$('#ordersgrid').datagrid('beginEdit',exsitEditindex);
			bindGridEvent();
		}
		
	});
	
	$('#ordersgrid').datagrid('reloadFooter',[{num:'合计',money:0}])
	$('#supplier').combogrid({    
		panelWidth:750,    
		idField:'uuid',    
		textField:'name',    
		url:'supplier_list?t1.type='+types,    
		columns:[[    
			{field:'uuid',title:'编号',width:60},    
			{field:'name',title:'名称',width:100},    
			{field:'address',title:'联系地址',width:120},    
			{field:'contact',title:'联系人',width:100},
			{field:'tele',title:'联系电话',width:100},
			{field:'email',title:'邮件地址',width:100}
			]],
		mode:'remote'
	});  
});




function getEditor(field){
	return  $('#ordersgrid').datagrid('getEditor', {index:exsitEditindex,field:field});
};


function cal(){
	var numEditor = getEditor('num');
	var num =  $(numEditor.target).val();
	var goodPriceEditor = getEditor('price');
	var price = $(goodPriceEditor.target).val();
	var moneyEditor = getEditor('money');
	var money = (num*price*1).toFixed(2);
	$(moneyEditor.target).val(money);
	$('#ordersgrid').datagrid('getRows')[exsitEditindex].money = money;
}

function bindGridEvent(){
	var numEditor = getEditor('num');
	$(numEditor.target).bind('keyup',function(){
		cal();
		sum();
	});
	var priceEditor = getEditor('price');
	$(priceEditor.target).bind('keyup',function(){
		cal();
		sum();
	});
}
function deleteRow(rowIndex){
	if(exsitEditindex>-1){
		$('#ordersgrid').datagrid('endEdit',exsitEditindex);
	};
	$('#ordersgrid').datagrid('deleteRow',rowIndex);
	var data = $('#ordersgrid').datagrid('getData');
	$('#ordersgrid').datagrid('loadData',data);
	sum();
}

function sum(){
	var rows = $('#ordersgrid').datagrid('getRows');
	var total = 0;
	$.each(rows,function(i,row){
		total += parseFloat(row.money);
	})
	$('#ordersgrid').datagrid('reloadFooter',[{num:'合计',money:total.toFixed(2)}]);
}

