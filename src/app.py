from flask import Flask, request
import urllib.request
import json

app = Flask(__name__)

@app.route("/champ", methods=['GET'])
def main_route():
    args = request.args
    champ_name = args.get("champ", "")
    version = args.get("version", "")
    contents = urllib.request.urlopen(f"http://ddragon.leagueoflegends.com/cdn/{version}/data/en_US/champion/{champ_name}.json").read()
    champ_info = json.loads(contents)
    return str(champ_info['data'][champ_name])


if __name__ == "__main__":
    app.run(port=6000)
