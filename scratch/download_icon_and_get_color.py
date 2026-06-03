import urllib.request
import os

urls = [
    "https://cdn.aksisoft.my.id/editor.id/2018/01/logonav.png",
    "https://cdn.aksisoft.my.id/editor.id/2025/11/69156ef950706-Logo-EDITOR.jpg",
    "https://i0.wp.com/cdn.aksisoft.my.id/editor.id/2018/01/logonav.png?fit=100%2C100&ssl=1",
    "https://i0.wp.com/cdn.aksisoft.my.id/editor.id/2018/01/logonav.png?w=100&ssl=1"
]

dest_path = r"c:\Users\USER\Desktop\risa.titip\antigrav-newsapp\app\src\main\res\drawable\ic_launcher_foreground.png"

os.makedirs(os.path.dirname(dest_path), exist_ok=True)
success = False

for url in urls:
    try:
        print("Trying url:", url)
        # Use headers that look exactly like a real browser
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8',
            'Referer': 'https://editor.id/'
        }
        req = urllib.request.Request(url, headers=headers)
        with urllib.request.urlopen(req, timeout=10) as response:
            data = response.read()
            with open(dest_path, 'wb') as f:
                f.write(data)
        print("Downloaded successfully from:", url, "bytes:", len(data))
        success = True
        break
    except Exception as e:
        print("Failed for", url, "error:", e)

if success:
    try:
        from PIL import Image
        img = Image.open(dest_path)
        img = img.convert('RGBA')
        colors = img.getcolors(10000)
        sorted_colors = sorted(colors, key=lambda x: x[0], reverse=True)
        print("Dominant colors:")
        count = 0
        for col in sorted_colors:
            num, rgba = col
            r, g, b, a = rgba
            # We want colors that are:
            # - Not transparent (a > 100)
            # - Not white (r < 240 or g < 240 or b < 240)
            # - Not black (r > 15 or g > 15 or b > 15)
            if a > 100 and (r < 240 or g < 240 or b < 240) and (r > 15 or g > 15 or b > 15):
                hex_color = f"#{r:02X}{g:02X}{b:02X}"
                print(f"Color: {hex_color}, count: {num}")
                count += 1
                if count >= 10:
                    break
    except Exception as ex:
        print("Color analysis skipped:", ex)
else:
    print("Could not download any icon.")
