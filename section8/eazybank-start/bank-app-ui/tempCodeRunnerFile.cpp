// The quick brown fox jumps over the lazy dog.

#include<bits/stdc++.h>
using namespace std;

int firstChar(string str) {
	unordered_map<char, int> map;

	for(int i = 0; i < str.size(); i++) {
		map[str[i]]++;
	}

    for(char c : str) {
        if(c == '' && map[c] == 1) {
            return c;
        }
    }
    return 0;
}

int main() {
    string str = "the quick brown fox jumps over the lazy dog."
    int ans = firstChar(str);
    cout << ans << endl;

    return 0;
}
