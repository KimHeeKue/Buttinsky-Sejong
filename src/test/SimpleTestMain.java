package test;

import java.io.FileInputStream;
import java.nio.file.Files;

import org.json.JSONObject;

import kr.unifox.sejong.simplified.DoubtfulText;
import kr.unifox.sejong.simplified.Mistake;
import kr.unifox.sejong.simplified.regex.RegexTester;

public class SimpleTestMain {

	public static void main(String[] args) {
		
		System.out.println("ㅇㅏㄴㄴㅕㅇ ㄷㅙㄱㅗ ㅇㅗㄹㅐㅅㅁㅏㄴ ㅇㅞㄴㅈㅣ ㄱㅣㅂㅜㄴㅇㅣ ㅈㅗㅎㅇㅡㄴㄱㅓㄹ ㅇㅏㄶㅎㅏㅁㅕㄴ ㄷㅚㄱㅔㅆㄷㅏ"
				.replaceAll("ㅇㅏㄶㅎ", "ㅇㅏㄴㅎ"));
		try {
			RegexTester tester = RegexTester.create(new FileInputStream("db/simplified.json"));
			DoubtfulText text = tester.test("않해 않돼어 않되어 돼고 또돼고 않돼고도 어떻해 오랫만 몇일 불리운 낳아 어의 금새 담배를 피우다 안돼");
			
			System.out.println("원본: " + text.originalText);
			System.out.println("수정본: " + text.replacedText);
			System.out.println("실수들: " + text.mistakeList.size() + "개");
			
			for(Mistake mistake : text.mistakeList)
			{
				String replaced = mistake.getReplaced();
				
				System.out.printf("%s: %s. %s %s(%d~%d)\n", 
						mistake.getID(), mistake.getTitle(),
						replaced == null? "실수" : replaced + "로 교체됨",
						mistake.getDescription(), 
						mistake.getStartIndex(),
						mistake.getEndIndex()
						);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}