/*[config]
<plugin name="百度地图,10" group="地图公交,7" devicetype="android" pump="usb,wifi,mirror,bluetooth,chip,Raid" icon="\icons\baiduMap.png" app="com.baidu.BaiduMap" version="7.6.0" description="百度地图" data="$data,ComplexTreeDataSource" >
<source>
<value>/data/data/com.baidu.BaiduMap/files/poi_his.sdb</value>
<value>/data/data/com.baidu.BaiduMap/files/route_his.sdb</value>
<value>/data/data/com.baidu.BaiduMap/files/fav_poi.sdb</value>
<value>/data/data/com.baidu.BaiduMap/files/fav_route.sdb</value>
</source>

<data  type="Search">
<item name="历史搜索地址" code="Addr" type="string" width="200" format=""></item>
<item name="时间" code="Time" type="datetime" width="140" format="yyyy-MM-dd HH:mm:ss" order="asc"></item>
<item name="搜索方式" code="Way" type="string" width="100" format=""></item>
</data>

<data  type="Searchpoi">
<item name="历史搜索地址" code="Addr" type="string" width="300" format=""></item>
<item name="时间" code="Time" type="datetime" width="200" format="yyyy-MM-dd HH:mm:ss" order="asc"></item>
</data>

<data  type="Searchroute">
<item name="搜索起点" code="Start" type="string" width="300" format=""></item>
<item name="搜索终点" code="Dest" type="string" width="300" format=""></item>
<item name="时间" code="Time" type="datetime" width="140" format="yyyy-MM-dd HH:mm:ss" order="asc"></item>
</data>

<data  type="Favorite">
<item name="详细地址" code="Addr" type="string" width="200" format=""></item>
<item name="经度" code="Longitude" type="string" width="120" format=""></item>
<item name="纬度" code="Latitude" type="string" width="120" format=""></item>
<item name="时间" code="Time" type="datetime" width="140" format="yyyy-MM-dd HH:mm:ss" order="asc"></item>
</data>

<data type="Route">
<item name="行进路线" code="Instructions" type="string" width="400" format=""></item>
</data>

<data  type="Navigate">
<item name="导航起点" code="Start" type="string" width="200" ></item>
<item name="导航终点" code="Dest" type="string" width="200" format=""></item>
<item name="时间" code="Time" type="datetime" width="140" format="yyyy-MM-dd HH:mm:ss" order="asc"></item>
<item name="导航方式" code="Type" type="string" width="100" format=""></item>
</data>

<data  type="Solution">
<item name="方案" code="Plan" type="string" width="200" format=""></item>
<item name="与目标地的距离" code="Dis" type="string" width="200" format=""></item>
</data>

</plugin>
[config]*/

// js content

//定义数据结构
function Navigate() {
    this.Start = "";
    this.Dest = "";
    this.Time = null;
    this.Type = "";
}

function Search() {
    this.Addr = "";
    this.Time = null;
    this.Way = "";
}

function Searchpoi() {
    this.Addr = "";
    this.Time = null;
}

function Searchroute() {
    this.Start = "";
    this.Dest = "";
    this.Time = null;
}

function Solution() {
    this.Dis = "";
    this.Plan = ""
}

function Route() {
    this.Instructions = "";
}



function Favorite() {
    this.Addr = "";
    this.Longitude = "";
    this.Latitude = "";
    this.Time = null;
}
//获取到达目的地方案信息
function GetSolution(infos, num) {
    var leg = infos.legs;
    var list = new Solution();
    list.Dis = leg.distance;
    list.Plan = "方案" + (++num);
    return list;
}
//坐标转换
function LocationInfo(x,y){
    var a = x / 20037508.34 * 180;
    var b = y / 20037508.34 * 180;
    var c = 180 / Math.PI * (2 * Math.atan(Math.pow(Math.E, b * Math.PI / 180)) - Math.PI / 2);
    var info = [a,c+0.1675];
    return info;
}
//获取导航地址信息
function GetNavigate(value, time) {
    var list = new Navigate();
    var dest = value.Fav_Sync.endnode;
    var start = value.Fav_Sync.startnode;
    var startlocation = LocationInfo(start.x,start.y);
    var destlocation = LocationInfo(dest.x,dest.y);
    list.Start = start.usname + "(" + "经度" + startlocation[0] + ";" + "纬度" + startlocation[1] + ")";
    list.Dest = dest.usname + "(" + "经度" + destlocation[0] + ";" + "纬度" + destlocation[1] + ")";
    list.Time = XLY.Convert.LinuxToDateTime(time);
    list.Type = GetString(value.Fav_Sync.uspathname);
    return list;
}
//获取公交导航路线信息
function GetRoute(infos) {
    var line = infos.busline;
    var list = new Route();
    var info = line[0];
    if (info.instructions != null) {
        list.Instructions = info.instructions;
    }
    return list;
}
//获取步行或驾车路线信息
function GetLine(infos) {
    var list = new Route();
    if (infos.description != null) {
        list.Instructions = infos.description;
    }
    if (infos.instructions != null) {
        list.Instructions = infos.instructions;
    }
    return list;
}
//获取搜索地点的信息
function GetSearchp(poi) {
    var arr = new Array();
    for (var p in poi) {
        var data = ExecEval(poi[p].value);
        var list = new Search();
        list.Addr = data.Fav_Content;
        list.Time = XLY.Convert.LinuxToDateTime(data.Fav_Sync.addtimesec);
        list.Way = "地点搜索";
        arr[p] = list;
    }
    return arr;
}

function GetSearchpoi(infos) {
    var list = new Searchpoi();
    list.Addr = infos.key;
    var time = ExecEval(infos.value);
    list.Time = XLY.Convert.LinuxToDateTime(time.Fav_Sync.addtimesec);
    return list;
}
//获取搜索路线信息
function GetSearchr(route) {
    var arr = new Array();
    for (var r in route) {
        var list = new Search();
        var datar = ExecEval(route[r].value);
        var me = ExecEval(datar.Fav_Content);
        list.Addr = me.efavnode.name;
        list.Time = XLY.Convert.LinuxToDateTime(me.addtimesec);
        list.Way = "路线搜索";
        arr[r] = list;
    }
    return arr;
}

//获取搜索路线信息
function GetSearchroute(infos) {
    var list = new Searchroute();
    var data = ExecEval(infos.value);
    var me = ExecEval(data.Fav_Content);
    var startlocation = LocationInfo(me.sfavnode.geoptx,me.sfavnode.geopty);
    var destlocation = LocationInfo(me.efavnode.geoptx,me.efavnode.geopty);
    list.Start = me.sfavnode.name + "(" + "经度" + startlocation[0] + ";" + "纬度" + startlocation[1] + ")";
    list.Dest = me.efavnode.name + "(" + "经度" + destlocation[0] + ";" + "纬度" + destlocation[1] + ")";
    list.Time = XLY.Convert.LinuxToDateTime(me.addtimesec);
    return list;
}

//获取收藏夹信息
function GetFavorite(infos) {
    var arr = new Array();
    for (var index in infos) {
        var data = infos[index].value;
        var info = ExecEval(data);
        var con = info.Fav_Sync;
        if (con != null) {
            var list = new Favorite();
            list.Addr = con.uspoiname;
            var location = LocationInfo(con.pt.x,con.pt.y);
            list.Longitude = location[0];
            list.Latitude = location[1];
            list.Time = XLY.Convert.LinuxToDateTime(infos.key);
            arr.push(list);
        }
    }
    return arr;
}

//创建简单的一级树模型
function BuildSimpleTree(treeinfos, treename, method) {
    for (var index in treeinfos) {
        var list = treeinfos[index];
        var treeinfo = method(list);
        treename.Items.push(treeinfo);
    }
}

//创建并获得导航路线信息树
function BuildSolutionTree(treeinfos, treename) {
    for (var index in treeinfos) {
        var list = treeinfos[index];
        var treeinfo = GetSolution(list, index);
        treename.Items.push(treeinfo);
    }
}

//json化字符串
function ExecEval(object) {
    var infos = eval('(' + object + ')');
    return infos;
}

//处理通过SQL语句查询
function ExecSql(path, sql) {
    var infos = eval('(' + XLY.Sqlite.Find(path, sql) + ')');
    return infos;
}

//获取字符串的特定子串
function GetString(str) {
    var newstr = str.slice(0, 2);
    return newstr;

}

//合并两条json数组
function objConcat(a1, a2) {
    var newarr = new Array();
    var p = 0;
    for (var k1 in a1) {
        newarr[k1] = a1[k1];
        p = k1;
    }
    p = parseInt(p) + 1;
    for (var k2 in a2) {
        newarr[p + parseInt(k2)] = a2[k2];
    }
    return newarr;
}

//树形结构
function TreeNode() {
    this.Text = ""; //节点名称
    this.TreeNodes = new Array(); //子节点数字
    this.Items = new Array(); //该节点的数据项，即前面定义的Item对象数组。
    this.Type = ""; //节点[Items]的数据类型
}

var result = new Array();
var source = $source;
var poihispath = source[0];
var routehispath = source[1];
var favpath = source[2];
var navipath = source[3];

//定义和查询历史搜索的信息
var psql = "select id,key,cast(value as text)as value from poi_his order by id";
var poihisdata = ExecSql(poihispath, psql);
var routehissql = "select id,key,cast(value as text)as value from route_his order by id";
var routehisdata = ExecSql(routehispath, routehissql);

//创建历史搜索树
var searchTree = new TreeNode();
searchTree.Text = "历史搜索";
searchTree.Type = "Search";
var searchinfosp = GetSearchp(poihisdata);
var searchinfosr = GetSearchr(routehisdata);
var searchdata = objConcat(searchinfosp, searchinfosr);
searchTree.Items = searchdata;

//创建地点搜索树
var poihisTree = new TreeNode();
poihisTree.Text = "地点搜索";
poihisTree.Type = "Searchpoi";
BuildSimpleTree(poihisdata, poihisTree, GetSearchpoi);

//创建路线搜索树
var routehisTree = new TreeNode();
routehisTree.Text = "路线搜索";
routehisTree.Type = "Searchroute";
BuildSimpleTree(routehisdata, routehisTree, GetSearchroute);
searchTree.TreeNodes.push(poihisTree);
searchTree.TreeNodes.push(routehisTree);

//创建收藏点树结构并获取值
var fsql = "select key,cast(value as text)as value from fav_poi";
var favdata = ExecSql(favpath, fsql);
var favoriteTree = new TreeNode();
favoriteTree.Text = "收藏地点";
favoriteTree.Type = "Favorite";
favoriteTree.Items = GetFavorite(favdata);

//定义导航树结构
var navisql = "select key,cast(value as text)as value from fav_route";
var nividata = ExecSql(navipath, navisql);
var navigateTree = new TreeNode();
navigateTree.Text = "导航信息";
navigateTree.Type = "Navigate";

//处理导航路线的数据结构
for (var index in nividata) {
    var infos = nividata[index].value;
    var time = nividata[index].key
    var value = ExecEval(infos);

    if (value.Fav_Sync != null) {
        var naviinfo = GetNavigate(value, time);
        navigateTree.Items.push(naviinfo);

        //提取公交方式导航的信息
        if (value.Fav_Sync.eplankind == 3) {
            var content = value.Fav_Content;
            var con = eval('(' + content + ')');
            var solutionTree = new TreeNode();
            solutionTree.Text = value.Fav_Sync.endnode.usname + "(" + GetString(value.Fav_Sync.uspathname) + ")";
            solutionTree.Type = "Solution";
            BuildSolutionTree(con.routes, solutionTree, GetSolution);
            for (var i in con.routes) {
                var step = con.routes[i].legs.steps;
                var routeTree = new TreeNode();
                routeTree.Text = "方案" + (++i);
                routeTree.Type = "Route";
                BuildSimpleTree(step, routeTree, GetRoute);
                solutionTree.TreeNodes.push(routeTree);
            }
        }

        //提取步行或驾车导航的信息
        if (value.Fav_Sync.eplankind == 0) {
            var content = value.Fav_Content;
            var con = eval('(' + content + ')');
            //XLY.Debug.WriteLine(con.contents.content.steps );
            var solutionTree = new TreeNode();
            solutionTree.Text = value.Fav_Sync.endnode.usname + "(" + GetString(value.Fav_Sync.uspathname) + ")";
            solutionTree.Type = "Route";
            if(con != null){
                if (con.routes != null) {
                    log(con.routes);
                    var step = con.routes.legs.steps;
                    BuildSimpleTree(step, solutionTree, GetLine);
                }

                if (con.contents != null) {
                    var line = con.contents.content.steps;
                    BuildSimpleTree(line, solutionTree, GetLine);
                }
            }
        }
        navigateTree.TreeNodes.push(solutionTree);
    }
}

//返回各级树的信息
result.push(searchTree);
result.push(favoriteTree);
result.push(navigateTree);

//返回该APP的所有信息
var res = JSON.stringify(result);
res;