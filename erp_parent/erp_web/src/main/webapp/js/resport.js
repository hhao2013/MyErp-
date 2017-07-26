$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:'resport_resport',
		columns:[[
  		    {field:'name',title:'商品类型',width:100},
  		    {field:'y',title:'销售额',width:100}
			]],
		singleSelect: true,
		onLoadSuccess:function(data){
			showChar(data.rows);
		}
	});
	
	$("#btnSearch").bind('click', function() {
		var sumbitData = $('#searchForm').serializeJSON();
		if(sumbitData.endtime!=""){
			sumbitData.endtime = sumbitData.endtime +" 23:59:59";
		}
		$('#grid').datagrid('load',sumbitData);
	});
	
});


function showChar(data){
	 $('#pieChart').highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            type: 'pie'
	        },
	        title: {
	            text: '销售额分析图'
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    }
	                }
	            }
	        },
	        series: [{
	            name: "所占比例",
	            colorByPoint: true,
	            data: data
	        }],
	        credits: {
		    	href: "www.itheima.com",
		    	text: "www.itheima.com"
	    	}
	    });
}