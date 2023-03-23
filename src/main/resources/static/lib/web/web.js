// 系统配置js文件

//加载页面 
var loading = layer.load();
$(document).ready(function () {
	layer.close(loading);
	$('#app').css('display', 'block')
})
// 设置 标签
function setTabs(e) {
	vm.tabs = e
	vm.$set(vm.tabs, 1, e)
}
// 设置导航栏
function setConfig() {
	 //获取静态文件
	var url = '/static/lib/web/web.json'
	var web = getAjax(url,'get','');
	// console.log(web)
	return web
}

const config = setConfig()

function checkTab(id) { //检查tabs是否已经打开
	var i = 0;
	var back = 0
	var tabs = vm.tabs
	for (i; i < tabs.length; i++) {
		if (tabs[i].id == id) {
			back = 0
			break //找到id 中断循环
		} else {
			back = 1
		}
	}
	return back
}

//计算 tab距离
function calTabWid(id) {
	// console.log(id)
	var tbox = $('.tab-item-box').width() //外边盒子距离长度
	var ul = $('.tab-item-ul').width() //tabs外边盒子长度
	var left = $('.tab-item-ul').position().left; //ul 左边偏移距离
	var titem = $('#' + id)
	if (titem.length > 0) {
		var iLeft = titem.position().left //tab距离父盒子左边距离
		var titemW = $('#' + id).width()
		if (ul > tbox && left <= 0) {
			var disAreaL = -left //可视区域左边
			var disAreaR = -left + tbox //可视区域右边
			var iRight = titemW + iLeft
			if (iLeft >= disAreaL && iRight <= disAreaR) {
				// console.log('不需要移动')
			} else {
				// console.log('需要移动')
				if (iLeft < disAreaL) {
					// console.log('往右移动')
					$('.tab-item-ul').css("left", left + (disAreaL - iLeft) + 50)
				} else if (iRight > disAreaR) {
					// console.log('往左移动')
					$('.tab-item-ul').css("left", left + (disAreaR - iRight) - 20)
				}
			}
		}
	}
}


// opentab 打开时计算左移距离
function calWidth() {
	var tbox = $('.tab-item-box').width() //外边盒子距离长度
	var titem = $('.tab-item')
	var len = vm.tabs.length + 2 //有几个tabs
	var countLen = titem.width() * len
	var ul = $('.tab-item-ul').width() + 100
	if (tbox - ul < 0) {
		var len = tbox - ul
		$('.tab-item-ul').css("left", len - 20)
	}
}

// 关闭当前tab标签  --> 下拉点击事件、tab X按键关闭事件
function closeTabThis(id) {
	var tabs = vm.tabs
	var i = 0;
	var len = vm.tabs.length - 2
	if (id == 0) {
		layer.msg('这个不能关闭的哦')
		return
	}
	for (i; i < tabs.length; i++) { //找到id在tabs数组中的位置 下标
		// console.log(tabs[i].id)
		if (tabs[i].id == id) {
			break;
		}
	}
	// console.log(i)
	vm.tabs.splice(i, 1) //从tabs中移除

	if (tabs.length == 0) { //如果tabs没有数据，将控制台设置变色
		vm.welcome = true
		vm.id = '0'
	} else {
		if (tabs[len].id != vm.id) { //判断tabs数组中最后一组数据的id是否为当前的id，若不是则设置
			vm.id = tabs[len].id
			vm.item = tabs[len].data
		}
	}
	//调整位置
	closeWidth()
}
//关闭其他标签页
function closeTabOther() { //删除tabs所有数组，重新push
	if (vm.tabs.length == 0) {
		return
	} else {
		if (vm.id == 0) {
			vm.tabs = []
		} else {
			var item = vm.item
			vm.tabs = []
			vm.tabs.push({ id: item.id, data: item })
			$('.tab-item-ul').css("left", 0)
		}
	}
}
//关闭所有标签页
function closeTabAll() {
	vm.tabs = []
	vm.welcome = true
	vm.id = '0'
	$('.tab-item-ul').css("left", 0)
}
// 关闭tabs时重新调整位置
function closeWidth() {
	var tbox = $('.tab-item-box').width() //外边盒子距离长度
	var ul = $('.tab-item-ul').width() //tabs外边盒子长度
	var left = $('.tab-item-ul').position().left; //ul 左边偏移距离
	if (ul > tbox) {
		$('.tab-item-ul').css("left", tbox - ul + 60)
	} else {
		$('.tab-item-ul').css("left", 0)
	}
}

// ajax 获取数据
function getAjax(url,type,param) {
	var data = null;
	$.ajax({
		url: url, //json文件路径
		async: false,
		type:type,
		data:param,
		success: function (e) { //成功
			data = e
		},
		error: function (e) { //失败
			console.log('ajax加载失败')
		},
	});
	return data;
}
