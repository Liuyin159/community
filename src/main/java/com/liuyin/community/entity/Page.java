package com.liuyin.community.entity;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -17:36
 * @Description
 * 封装分页相关信息
 */
public class Page {
    //当前页码
    private int current = 1;
    //显示上限
    private int limit = 10;
    //数据总数(计算总页数)
    private int rows;
    //查询路径(用于复用分页连接)
    private String path;
    
    public int getCurrent(){
        return current;
    }
    
    public int getLimit() {
        return limit;
    }

    public int getRows() {
        return rows;
    }

    public String getPath() {
        return path;
    }

    public void setCurrent(int current) {
        if(current >= 1){

            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <=100){

            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if(rows >= 0){

            this.rows = rows;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }
    /**
     * 获取当前页的起始行
     */
    
    public int getOffset(){
        return (current-1)*limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal(){
        if(rows % limit == 0){
            return rows/limit;
        }else {
            return rows/limit + 1;
        }
    }

    /**
     * 获取起始页码，即当前页可以看到的其他页码的最小的
     */
    public  int getFrom(){
        int from = current - 2;
        return from < 1? 1 : from;
    }

    /**
     * 获取结束页码，即当前页可以看到的最大的页码
     * 
     */
    public int getTo(){
        int to = current + +2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
