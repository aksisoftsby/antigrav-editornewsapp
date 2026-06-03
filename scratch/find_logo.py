import urllib.request
import re

url = "https://editor.id/"
try:
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'}
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as response:
        html = response.read().decode('utf-8')
    
    print("HTML loaded successfully. Length:", len(html))
    
    # Find all icon links
    icons = re.findall(r'<link[^>]+rel=["\'](?:shortcut )?icon["\'][^>]+href=["\']([^"\']+)["\']', html, re.I)
    icons += re.findall(r'<link[^>]+href=["\']([^"\']+)["\'][^>]+rel=["\'](?:shortcut )?icon["\']', html, re.I)
    
    # Find apple touch icon
    icons += re.findall(r'<link[^>]+rel=["\']apple-touch-icon(?:-precomposed)?["\'][^>]+href=["\']([^"\']+)["\']', html, re.I)
    
    # Find logo images
    logos = re.findall(r'<img[^>]+src=["\']([^"\']*(?:logo|brand|favicon)[^"\']*)["\']', html, re.I)
    
    print("Found icons:", icons)
    print("Found logos:", logos)
    
except Exception as e:
    print("Error:", e)
