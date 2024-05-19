import requests
from bs4 import BeautifulSoup
import time
import random
urls=['https://movie.douban.com/subject/34841067/comments?start={}&limit=20&status=P&sort=new_score'.format(str(i)) for i in range(0, 200, 20)] #通过观察的url翻页的规律，使用for循环得到10个链接，保存到urls列表中
print(urls)
dic_h = {
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"}
comments_list = [] #初始化用于保存短评的列表

for url in urls: #使用for循环分别获取每个页面的数据，保存到comments_list列表
    r = requests.get(url=url,headers = dic_h).text

    soup = BeautifulSoup(r, 'lxml')
    ul = soup.find('div',id="comments")
    lis= ul.find_all('p')

    list2 =[]
    for li in lis:
        list2.append(li.find('span').string)
    # print(list2)
    comments_list.extend(list2)
    time.sleep(random.randint(0,3)) # 暂停0~3秒
    
with open('lhy_comments.txt', 'w', encoding='utf-8') as f: #使用with open()新建对象f
    # 将列表中的数据循环写入到文本文件中
    for i in comments_list:
        f.write(i+"\n") #写入数据


