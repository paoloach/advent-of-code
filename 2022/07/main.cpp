#include <iostream>
#include <fstream>
#include <string_view>
#include <vector>
#include <map>

using namespace std;

class ElfDir {
    public:
        ElfDir(ElfDir * parent, string path):
            parent(parent), 
            path(path), 
            files( map<string, long>() ),
            children(map<string, ElfDir *>() )
            {}

        ElfDir * parent;
        string path;
        map<string, long> files;
        map<string, ElfDir *> children;

        string getFullPath() {
            if (parent == nullptr)
                return "";
            return parent->getFullPath() + "/" + path;
        }

        long dirSize() {
            long fileSize = 0;
            for ( auto &[name, size] : files){
                fileSize += size;
            }
            long dirsSize = 0;
            for ( auto &[name, child] : children){
                dirsSize += child->dirSize();
            }
            return fileSize + dirsSize;
        }
    
};

int main() {

    ifstream infile("./input.txt");
    string fileLine;


    auto rootDir = ElfDir{nullptr, "/"};
    auto allDir = map<string, ElfDir *>{};

    auto currentDir = &rootDir;

    while (std::getline(infile, fileLine)) {
        std::string_view line {fileLine};
        if (line.substr(0,4)=="$ cd"){
            auto cdDest = line.substr(5);
            if (cdDest == "/"){
                currentDir = &rootDir;
            } else if (cdDest == ".."){
                currentDir = currentDir->parent;
            } else {
                auto dirName = string(cdDest);
                auto child = currentDir->children[dirName];
                if (child == nullptr){
                    child = new ElfDir{currentDir, dirName};
                }
                currentDir->children[dirName] = child;
                allDir[child->getFullPath()] = child;
                currentDir = child;
            }
        } else if (line.substr(0, 4) == "$ ls"){
            // skip
        }else if (line.substr(0, 3) == "dir"){
            
        } else {
            auto posSpace = line.find(" ");
            auto size = line.substr(0, posSpace);
            auto name = line.substr(posSpace+1);
            currentDir->files[string(name)] = atol(size.begin());
        }
    }


    long totalSize = 0;
    for (auto & [name, dir] : allDir){
        int dirSize = dir->dirSize();
        if (dirSize < 100000){
            totalSize += dirSize;
        }
    }


    auto rootSize = rootDir.dirSize();
    auto available = 70000000-rootSize;
    auto need = 30000000-available;

    long minDirSize = rootSize;
    for (auto & [name, dir] : allDir){
        int dirSize = dir->dirSize();
        if (dirSize >= need &&  dirSize < minDirSize){
            minDirSize = dirSize;
        }
    }


    std::cout << "step1: " << totalSize << endl;
    std::cout << "step2: " << minDirSize << endl;
}