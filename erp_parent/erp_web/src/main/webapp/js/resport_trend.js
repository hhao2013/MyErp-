$(function(){
	//加载表格数据
	$('#grid').datagrid({
		columns:[[
  		    {field:'month',title:'月份',width:100},
  		    {field:'y',title:'销售额',width:100}
			]],
		singleSelect: true,
		onLoadSuccess:function(data){
			showChar(data.rows);
		}
	});
	
	var date = new Date();
	$('#yearChecked').combobox('setValue', date.getFullYear());
	$('#grid').datagrid(
			{
				url:'resport_trendResport',
				queryParams: {
					year:date.getFullYear()
				}
			}
	);
	$("#btnSearch").bind('click', function() {
		var sumbitData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',sumbitData);
	});
	
});


function showChar(data){
	var yearMonth = [];
	for(var i =1; i<=12;i++){
		yearMonth.push(i+'月');
	}
	 $('#pieChart').highcharts({
	        title: {
	            text: '年度销售趋势分析图',
	            x: -20 //center
	        },
	        subtitle: {
	            text: 'Source: www.itheima.com',
	            x: -20
	        },
	        xAxis: {
	            categories:yearMonth
	        },
	        yAxis: {
	            title: {
	                text: '销售额（RMB）'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: '￥'
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: [{
	            name: '月度销售额',
	            data: data
	        }], credits: {
		    	href: "www.itheima.com",
		    	text: "www.itheima.com"
	    	},
	    });
}