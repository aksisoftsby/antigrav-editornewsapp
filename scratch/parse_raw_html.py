import urllib.request
import re

url = "https://editor.id/"
try:
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'}
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as response:
        html = response.read().decode('utf-8')
    
    print("Finding all link tags:")
    links = re.findall(r'<link[^>]+>', html)
    for link in links:
        if 'icon' in link or 'shortcut' in link or 'apple-touch' in link:
            print(link)
            
    print("\nFinding some img tags:")
    imgs = re.findall(r'<img[^>]+>', html)
    for img in imgs[:10]:
        print(img)
        
except Exception as e:
    print("Error:", e)
