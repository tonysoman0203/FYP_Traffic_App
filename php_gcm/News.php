<?php

/**
 * Created by PhpStorm.
 * User: soman
 * Date: 2016/3/23
 * Time: 14:57
 */
class News
{
    private $title;
    private $data;
    private $is_background;

    public function getNews(){
        $res = array();
        $res['title'] = $this->title;
        $res['is_background'] = $this->is_background;
        $res['data'] = $this->data;

        return $res;
    }
    
    /**
     * News constructor.
     */
    public function __construct()
    {
    }


    /**
     * @return mixed
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * @param mixed $title
     */
    public function setTitle($title)
    {
        $this->title = $title;
    }

    /**
     * @return mixed
     */
    public function getData()
    {
        return $this->data;
    }

    /**
     * @param mixed $data
     */
    public function setData($data)
    {
        $this->data = $data;
    }

    /**
     * @return mixed
     */
    public function getIsBackground()
    {
        return $this->is_background;
    }

    /**
     * @param mixed $is_background
     */
    public function setIsBackground($is_background)
    {
        $this->is_background = $is_background;
    }

}